package edu.cit.poliquit.aquahaven.service;

import edu.cit.poliquit.aquahaven.dto.ApiResponse;
import edu.cit.poliquit.aquahaven.dto.ProductDto.ProductResponse;
import edu.cit.poliquit.aquahaven.dto.ProductDto.ProductSearchRequest;
import edu.cit.poliquit.aquahaven.repository.ProductRepository;
import edu.cit.poliquit.aquahaven.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProductService {

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("name", "price", "createdAt");

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(ProductSearchRequest req) {

        @SuppressWarnings("unchecked")
        Specification<edu.cit.poliquit.aquahaven.entity.Product> spec =
                ProductSpecification.compose(
                        ProductSpecification.isAvailable(),
                        ProductSpecification.nameOrDescriptionContains(req.getKeyword()),
                        ProductSpecification.inCategorySlug(req.getCategorySlug()),
                        ProductSpecification.hasWaterType(req.getWaterType()),
                        ProductSpecification.priceBetween(req.getMinPrice(), req.getMaxPrice())
                );

        String sortField = ALLOWED_SORT_FIELDS.contains(req.getSortBy())
                ? req.getSortBy() : "createdAt";
        Sort.Direction dir = "asc".equalsIgnoreCase(req.getSortDir())
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageable = PageRequest.of(
                Math.max(req.getPage(), 0),
                req.getSize(),
                Sort.by(dir, sortField)
        );

        return productRepository
                .findAll(spec, pageable)
                .map(ProductResponse::from);
    }

    @Transactional(readOnly = true)
    public ApiResponse<ProductResponse> getById(Long id) {
        return productRepository.findById(id)
                .filter(edu.cit.poliquit.aquahaven.entity.Product::isActive)
                .map(p -> ApiResponse.<ProductResponse>ok(ProductResponse.from(p)))
                .orElseGet(() -> ApiResponse.fail("PRODUCT-404", "Product not found"));
    }
}