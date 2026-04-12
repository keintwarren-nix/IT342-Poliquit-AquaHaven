package edu.cit.poliquit.aquahaven.repository;

import edu.cit.poliquit.aquahaven.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    // JpaSpecificationExecutor gives us:
    // Page<Product> findAll(Specification<Product> spec, Pageable pageable)
    // — used by ProductSpecification (Strategy / Specification pattern)
}