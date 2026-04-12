package edu.cit.poliquit.aquahaven.service;

import edu.cit.poliquit.aquahaven.dto.ProductDto.CategoryResponse;
import edu.cit.poliquit.aquahaven.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Returns all active categories ordered by sortOrder.
     * Used by the frontend to populate the filter sidebar/chips —
     * NO hard-coding of category names in the UI.
     */
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }
}