package edu.cit.poliquit.aquahaven.product.repository;

import edu.cit.poliquit.aquahaven.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
                JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.createdAt DESC")
    Page<Product> findAllWithCategory(Pageable pageable);
}