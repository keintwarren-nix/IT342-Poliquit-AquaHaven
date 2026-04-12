package edu.cit.poliquit.aquahaven.controller;

import edu.cit.poliquit.aquahaven.dto.ApiResponse;
import edu.cit.poliquit.aquahaven.dto.ProductDto.ProductResponse;
import edu.cit.poliquit.aquahaven.dto.ProductDto.ProductSearchRequest;
import edu.cit.poliquit.aquahaven.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<Page<ProductResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) String waterType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ProductSearchRequest req = new ProductSearchRequest();

        req.setKeyword(keyword);
        req.setCategorySlug(categorySlug);
        req.setWaterType(waterType);
        req.setMinPrice(minPrice);
        req.setMaxPrice(maxPrice);
        req.setSortBy(sortBy);
        req.setSortDir(sortDir);
        req.setPage(page);
        req.setSize(size);

        return ApiResponse.ok(productService.search(req));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return productService.getById(id);
    }
}