package edu.cit.poliquit.aquahaven.admin.controller;

import edu.cit.poliquit.aquahaven.admin.dto.*;
import edu.cit.poliquit.aquahaven.category.repository.CategoryRepository;
import edu.cit.poliquit.aquahaven.common.response.ApiResponse;
import edu.cit.poliquit.aquahaven.order.entity.Order;
import edu.cit.poliquit.aquahaven.order.repository.OrderRepository;
import edu.cit.poliquit.aquahaven.product.dto.response.ProductResponse;
import edu.cit.poliquit.aquahaven.product.entity.Product;
import edu.cit.poliquit.aquahaven.product.repository.ProductRepository;
import edu.cit.poliquit.aquahaven.user.entity.User;
import edu.cit.poliquit.aquahaven.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ProductRepository  productRepository;
    private final OrderRepository    orderRepository;
    private final UserRepository     userRepository;
    private final CategoryRepository categoryRepository;

    public AdminController(ProductRepository productRepository,
                           OrderRepository orderRepository,
                           UserRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.productRepository  = productRepository;
        this.orderRepository    = orderRepository;
        this.userRepository     = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PRODUCTS
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/products")
    public ApiResponse<Page<ProductResponse>> listProducts(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ProductResponse> result = productRepository
                .findAllWithCategory(PageRequest.of(page, size))
                .map(ProductResponse::from);
        return ApiResponse.ok(result);
    }

    @PostMapping("/products")
    public ApiResponse<ProductResponse> createProduct(@RequestBody AdminProductRequest req) {
        if (req.getName() == null || req.getName().isBlank())
            return ApiResponse.fail("ADMIN-001", "Product name is required");
        if (req.getPrice() == null)
            return ApiResponse.fail("ADMIN-002", "Price is required");
        if (req.getCategoryId() == null)
            return ApiResponse.fail("ADMIN-003", "Category is required");

        var category = categoryRepository.findById(req.getCategoryId()).orElse(null);
        if (category == null)
            return ApiResponse.fail("ADMIN-004", "Category not found: " + req.getCategoryId());

        Product product = new Product();
        product.setName(req.getName().trim());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setImageUrl(req.getImageUrl());
        product.setWaterType(req.getWaterType());
        product.setCategory(category);
        product.setActive(req.isActive());

        Product saved = productRepository.save(product);
        return ApiResponse.ok(ProductResponse.from(saved));
    }

    @PutMapping("/products/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id,
                                                      @RequestBody AdminProductRequest req) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ApiResponse.fail("ADMIN-404", "Product not found: " + id);

        if (req.getCategoryId() != null) {
            var category = categoryRepository.findById(req.getCategoryId()).orElse(null);
            if (category == null)
                return ApiResponse.fail("ADMIN-004", "Category not found: " + req.getCategoryId());
            product.setCategory(category);
        }

        if (req.getName() != null && !req.getName().isBlank())
            product.setName(req.getName().trim());
        if (req.getDescription() != null)
            product.setDescription(req.getDescription());
        if (req.getPrice() != null)
            product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        if (req.getImageUrl() != null)
            product.setImageUrl(req.getImageUrl());
        if (req.getWaterType() != null)
            product.setWaterType(req.getWaterType());
        product.setActive(req.isActive());

        Product saved = productRepository.save(product);
        return ApiResponse.ok(ProductResponse.from(saved));
    }

    @DeleteMapping("/products/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id))
            return ApiResponse.fail("ADMIN-404", "Product not found: " + id);
        productRepository.deleteById(id);
        return ApiResponse.ok(null);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ORDERS
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/orders")
    public ApiResponse<Page<AdminOrderResponse>> listOrders(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)    String status) {

        PageRequest pageable = PageRequest.of(page, size);

        Page<AdminOrderResponse> result;
        if (status != null && !status.isBlank()) {
            try {
                Order.Status s = Order.Status.valueOf(status.toUpperCase());
                result = orderRepository.findByStatusWithItemsAndUser(s, pageable).map(AdminOrderResponse::from);
            } catch (IllegalArgumentException e) {
                return ApiResponse.fail("ADMIN-005", "Invalid status: " + status);
            }
        } else {
            result = orderRepository.findAllWithItemsAndUser(pageable).map(AdminOrderResponse::from);
        }

        return ApiResponse.ok(result);
    }

    @GetMapping("/orders/{ref}")
    public ApiResponse<AdminOrderResponse> getOrder(@PathVariable String ref) {
        return orderRepository.findByOrderRef(ref)
                .map(o -> ApiResponse.ok(AdminOrderResponse.from(o)))
                .orElseGet(() -> ApiResponse.fail("ADMIN-404", "Order not found: " + ref));
    }

    @PatchMapping("/orders/{ref}/status")
    public ApiResponse<AdminOrderResponse> updateOrderStatus(@PathVariable String ref,
                                                             @RequestBody AdminOrderStatusRequest req) {
        Order order = orderRepository.findByOrderRef(ref).orElse(null);
        if (order == null)
            return ApiResponse.fail("ADMIN-404", "Order not found: " + ref);

        try {
            order.setStatus(Order.Status.valueOf(req.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail("ADMIN-005", "Invalid status: " + req.getStatus());
        }

        Order saved = orderRepository.save(order);
        return ApiResponse.ok(AdminOrderResponse.from(saved));
    }

    @DeleteMapping("/orders/{ref}")
    public ApiResponse<Void> deleteOrder(@PathVariable String ref) {
        Order order = orderRepository.findByOrderRef(ref).orElse(null);
        if (order == null)
            return ApiResponse.fail("ADMIN-404", "Order not found: " + ref);
        orderRepository.delete(order);
        return ApiResponse.ok(null);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // USERS
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/users")
    public ApiResponse<Page<AdminUserResponse>> listUsers(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<AdminUserResponse> result = userRepository
                .findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(AdminUserResponse::from);
        return ApiResponse.ok(result);
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id))
            return ApiResponse.fail("ADMIN-404", "User not found: " + id);
        userRepository.deleteById(id);
        return ApiResponse.ok(null);
    }

    @PatchMapping("/users/{id}/role")
    public ApiResponse<AdminUserResponse> changeUserRole(@PathVariable Long id,
                                                         @RequestBody AdminRoleRequest req) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ApiResponse.fail("ADMIN-404", "User not found: " + id);

        String role = req.getRole() == null ? "" : req.getRole().toUpperCase();
        if (!role.equals("ADMIN") && !role.equals("CUSTOMER"))
            return ApiResponse.fail("ADMIN-006", "Role must be ADMIN or CUSTOMER");

        user.setRole("ROLE_" + role);
        User saved = userRepository.save(user);
        return ApiResponse.ok(AdminUserResponse.from(saved));
    }
}