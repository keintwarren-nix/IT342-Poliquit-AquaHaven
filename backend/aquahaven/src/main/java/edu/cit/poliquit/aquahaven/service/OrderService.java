package edu.cit.poliquit.aquahaven.service;

import edu.cit.poliquit.aquahaven.dto.ApiResponse;
import edu.cit.poliquit.aquahaven.dto.OrderDto.*;
import edu.cit.poliquit.aquahaven.entity.Order;
import edu.cit.poliquit.aquahaven.entity.Order.PaymentMethod;
import edu.cit.poliquit.aquahaven.entity.Order.Status;
import edu.cit.poliquit.aquahaven.entity.OrderItem;
import edu.cit.poliquit.aquahaven.entity.Product;
import edu.cit.poliquit.aquahaven.entity.User;
import edu.cit.poliquit.aquahaven.repository.OrderRepository;
import edu.cit.poliquit.aquahaven.repository.ProductRepository;
import edu.cit.poliquit.aquahaven.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository   orderRepository;
    private final UserRepository    userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        ProductRepository productRepository) {
        this.orderRepository   = orderRepository;
        this.userRepository    = userRepository;
        this.productRepository = productRepository;
    }

    // ── Place Order ───────────────────────────────────────────────────────────

    @Transactional
    public ApiResponse<OrderResponse> placeOrder(String email, PlaceOrderRequest req) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ApiResponse.fail("USER-404", "User not found");

        if (req.getItems() == null || req.getItems().isEmpty())
            return ApiResponse.fail("ORDER-001", "Order must contain at least one item");

        if (req.getShippingAddress() == null || req.getShippingAddress().isBlank())
            return ApiResponse.fail("ORDER-002", "Shipping address is required");

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(req.getPaymentMethod().toUpperCase());
        } catch (Exception e) {
            return ApiResponse.fail("ORDER-003", "Invalid payment method: " + req.getPaymentMethod());
        }

        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItemRequest ci : req.getItems()) {
            if (ci.getQuantity() <= 0)
                return ApiResponse.fail("ORDER-004", "Quantity must be at least 1 for product " + ci.getProductId());

            Product product = productRepository.findById(ci.getProductId()).orElse(null);
            if (product == null || !product.isActive())
                return ApiResponse.fail("ORDER-005", "Product not found: " + ci.getProductId());

            if (product.getStock() < ci.getQuantity())
                return ApiResponse.fail("ORDER-006",
                        "Insufficient stock for '" + product.getName() + "'. Available: " + product.getStock());

            product.setStock(product.getStock() - ci.getQuantity());
            productRepository.save(product);

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal  = unitPrice.multiply(BigDecimal.valueOf(ci.getQuantity()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(ci.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setSubtotal(subtotal);

            items.add(item);
            total = total.add(subtotal);
        }

        long seq = orderRepository.countAll() + 1;
        String orderRef = String.format("AH-%05d", seq);

        order.setOrderRef(orderRef);
        order.setUser(user);
        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus(Status.PENDING);
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(req.getShippingAddress());
        order.setNotes(req.getNotes());

        Order saved = orderRepository.save(order);
        return ApiResponse.ok(OrderResponse.from(saved));
    }

    // ── My Orders ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ApiResponse<List<OrderResponse>> myOrders(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ApiResponse.fail("USER-404", "User not found");

        List<OrderResponse> responses = orderRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.ok(responses);
    }

    // ── Get Single Order ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ApiResponse<OrderResponse> getOrder(String email, String ref) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ApiResponse.fail("USER-404", "User not found");

        return orderRepository.findByOrderRefAndUserId(ref, user.getId())
                .map(o -> ApiResponse.ok(OrderResponse.from(o)))
                .orElseGet(() -> ApiResponse.fail("ORDER-404", "Order not found: " + ref));
    }

    // ── Confirm Payment ───────────────────────────────────────────────────────

    @Transactional
    public ApiResponse<OrderResponse> confirmPayment(String email, String ref) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ApiResponse.fail("USER-404", "User not found");

        Order order = orderRepository.findByOrderRefAndUserId(ref, user.getId()).orElse(null);
        if (order == null) return ApiResponse.fail("ORDER-404", "Order not found: " + ref);

        if (order.getStatus() != Status.PENDING)
            return ApiResponse.fail("ORDER-007",
                    "Only PENDING orders can be confirmed. Current status: " + order.getStatus());

        order.setStatus(Status.PAID);
        Order saved = orderRepository.save(order);
        return ApiResponse.ok(OrderResponse.from(saved));
    }
}