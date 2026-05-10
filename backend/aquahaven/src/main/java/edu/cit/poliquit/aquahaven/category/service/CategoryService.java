package edu.cit.poliquit.aquahaven.category.service;

import edu.cit.poliquit.aquahaven.category.dto.response.CategoryResponse;
import edu.cit.poliquit.aquahaven.category.repository.CategoryRepository;
import edu.cit.poliquit.aquahaven.common.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryRepository
                .findByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.ok(categories);
    }
}