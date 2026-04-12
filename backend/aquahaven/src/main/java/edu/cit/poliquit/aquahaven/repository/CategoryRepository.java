package edu.cit.poliquit.aquahaven.repository;

import edu.cit.poliquit.aquahaven.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByActiveTrueOrderBySortOrderAsc();
    Optional<Category> findBySlug(String slug);
}