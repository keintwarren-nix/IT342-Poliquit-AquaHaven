package edu.cit.poliquit.aquahaven.order.controller;

import edu.cit.poliquit.aquahaven.common.response.ApiResponse;
import edu.cit.poliquit.aquahaven.order.dto.request.PlaceOrderRequest;
import edu.cit.poliquit.aquahaven.order.dto.response.OrderResponse;
import edu.cit.poliquit.aquahaven.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<OrderResponse> placeOrder(
            @RequestBody PlaceOrderRequest request,
            Principal principal) {
        return orderService.placeOrder(principal.getName(), request);
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> myOrders(Principal principal) {
        return orderService.myOrders(principal.getName());
    }

    @GetMapping("/{ref}")
    public ApiResponse<OrderResponse> getOrder(
            @PathVariable String ref,
            Principal principal) {
        return orderService.getOrder(principal.getName(), ref);
    }

    @PatchMapping("/{ref}/confirm")
    public ApiResponse<OrderResponse> confirmPayment(
            @PathVariable String ref,
            Principal principal) {
        return orderService.confirmPayment(principal.getName(), ref);
    }
}