package edu.cit.poliquit.aquahaven.controller;

import edu.cit.poliquit.aquahaven.dto.ApiResponse;
import edu.cit.poliquit.aquahaven.dto.ProductDto.CategoryResponse;
import edu.cit.poliquit.aquahaven.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll() {
        return ApiResponse.ok(categoryService.getActiveCategories());
    }
}