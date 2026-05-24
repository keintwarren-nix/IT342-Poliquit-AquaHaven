package edu.cit.poliquit.aquahaven.cart.controller;

import edu.cit.poliquit.aquahaven.cart.dto.request.AddToCartRequest;
import edu.cit.poliquit.aquahaven.cart.dto.request.UpdateCartItemRequest;
import edu.cit.poliquit.aquahaven.cart.dto.response.CartResponse;
import edu.cit.poliquit.aquahaven.cart.service.CartService;
import edu.cit.poliquit.aquahaven.common.response.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ApiResponse<CartResponse> getCart(Authentication authentication) {
        CartResponse cart = cartService.getCart(authentication);
        return ApiResponse.ok(cart);
    }

    @PostMapping("/items")
    public ApiResponse<CartResponse> addToCart(@RequestBody AddToCartRequest request,
                                                Authentication authentication) {
        CartResponse cart = cartService.addToCart(request, authentication);
        return ApiResponse.ok(cart);
    }

    @PutMapping("/items/{itemId}")
    public ApiResponse<CartResponse> updateCartItem(@PathVariable Long itemId,
                                                     @RequestBody UpdateCartItemRequest request,
                                                     Authentication authentication) {
        CartResponse cart = cartService.updateCartItem(itemId, request, authentication);
        return ApiResponse.ok(cart);
    }

    @DeleteMapping("/items/{itemId}")
    public ApiResponse<CartResponse> removeFromCart(@PathVariable Long itemId,
                                                     Authentication authentication) {
        CartResponse cart = cartService.removeFromCart(itemId, authentication);
        return ApiResponse.ok(cart);
    }

    @DeleteMapping
    public ApiResponse<CartResponse> clearCart(Authentication authentication) {
        CartResponse cart = cartService.clearCart(authentication);
        return ApiResponse.ok(cart);
    }
}
