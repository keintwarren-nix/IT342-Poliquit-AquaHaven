package edu.cit.poliquit.aquahaven.category.controller;

import edu.cit.poliquit.aquahaven.category.dto.response.CategoryResponse;
import edu.cit.poliquit.aquahaven.category.service.CategoryService;
import edu.cit.poliquit.aquahaven.common.response.ApiResponse;
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
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}