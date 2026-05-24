package edu.cit.poliquit.aquahaven.cart.service;

import edu.cit.poliquit.aquahaven.cart.dto.request.AddToCartRequest;
import edu.cit.poliquit.aquahaven.cart.dto.request.UpdateCartItemRequest;
import edu.cit.poliquit.aquahaven.cart.dto.response.CartItemResponse;
import edu.cit.poliquit.aquahaven.cart.dto.response.CartResponse;
import edu.cit.poliquit.aquahaven.cart.entity.Cart;
import edu.cit.poliquit.aquahaven.cart.entity.CartItem;
import edu.cit.poliquit.aquahaven.cart.repository.CartItemRepository;
import edu.cit.poliquit.aquahaven.cart.repository.CartRepository;
import edu.cit.poliquit.aquahaven.common.exception.BadRequestException;
import edu.cit.poliquit.aquahaven.common.exception.ResourceNotFoundException;
import edu.cit.poliquit.aquahaven.product.entity.Product;
import edu.cit.poliquit.aquahaven.product.repository.ProductRepository;
import edu.cit.poliquit.aquahaven.user.entity.User;
import edu.cit.poliquit.aquahaven.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = getOrCreateCart(user);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse addToCart(AddToCartRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }
        if (request.getQuantity() > product.getStock()) {
            throw new BadRequestException("Insufficient stock");
        }

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (newQuantity > product.getStock()) {
                throw new BadRequestException("Insufficient stock");
            }
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.getItems().add(newItem);
        }

        cart = cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItem(Long itemId, UpdateCartItemRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (request.getQuantity() <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            if (request.getQuantity() > item.getProduct().getStock()) {
                throw new BadRequestException("Insufficient stock");
            }
            item.setQuantity(request.getQuantity());
        }

        cart = cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse removeFromCart(Long itemId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cart = cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse clearCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cart = cartRepository.save(cart);
        return toCartResponse(cart);
    }

    private CartResponse toCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());

        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setProductImageUrl(item.getProduct().getImageUrl());
            itemResponse.setProductPrice(item.getProduct().getPrice());
            itemResponse.setQuantity(item.getQuantity());
            BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            itemResponse.setSubtotal(subtotal);
            total = total.add(subtotal);
            itemResponses.add(itemResponse);
        }

        response.setItems(itemResponses);
        response.setTotal(total);
        return response;
    }
}
