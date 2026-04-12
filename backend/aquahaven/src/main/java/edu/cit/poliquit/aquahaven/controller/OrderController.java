package edu.cit.poliquit.aquahaven.controller;

import edu.cit.poliquit.aquahaven.dto.ApiResponse;
import edu.cit.poliquit.aquahaven.dto.OrderDto.*;
import edu.cit.poliquit.aquahaven.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints require a valid JWT.
 * The authenticated user's email is extracted from the security context —
 * users can only see/modify their own orders.
 *
 * POST   /api/v1/orders              — place a new order
 * GET    /api/v1/orders              — list my orders
 * GET    /api/v1/orders/{ref}        — get order detail
 * POST   /api/v1/orders/{ref}/pay    — confirm payment (simulated)
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<OrderResponse> placeOrder(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody PlaceOrderRequest req) {
        return orderService.placeOrder(principal.getUsername(), req);
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> myOrders(
            @AuthenticationPrincipal UserDetails principal) {
        return orderService.myOrders(principal.getUsername());
    }

    @GetMapping("/{ref}")
    public ApiResponse<OrderResponse> getOrder(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable String ref) {
        return orderService.getOrder(principal.getUsername(), ref);
    }

    @PostMapping("/{ref}/pay")
    public ApiResponse<OrderResponse> confirmPayment(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable String ref) {
        return orderService.confirmPayment(principal.getUsername(), ref);
    }
}