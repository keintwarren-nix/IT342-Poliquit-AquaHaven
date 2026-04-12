package edu.cit.poliquit.aquahaven.specification;

import edu.cit.poliquit.aquahaven.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * DESIGN PATTERN — Specification (Behavioral / Strategy variant)
 *
 * Each static method returns an atomic predicate (Specification<Product>).
 * These predicates are composed at runtime using .and() / .or() chains,
 * letting the service build arbitrarily complex queries without any
 * hard-coded SQL or if-else chains in the service layer.
 *
 * This means:
 *  - Adding a new filter = adding one static method here, zero changes elsewhere.
 *  - Each predicate is independently unit-testable.
 *  - The query is built from DB metadata (category slugs), not hard-coded strings.
 */
public class ProductSpecification {

    private ProductSpecification() {}   // utility class

    /** Only show active, in-stock products */
    public static Specification<Product> isAvailable() {
        return (root, query, cb) ->
                cb.and(
                        cb.isTrue(root.get("active")),
                        cb.greaterThan(root.get("stock"), 0)
                );
    }

    /** Full-text search across name and description */
    public static Specification<Product> nameOrDescriptionContains(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        String pattern = "%" + keyword.toLowerCase() + "%";
        return (root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                );
    }

    /** Filter by category slug (comes from the DB-seeded categories table) */
    public static Specification<Product> inCategorySlug(String slug) {
        if (slug == null || slug.isBlank() || slug.equals("all")) return null;
        return (root, query, cb) ->
                cb.equal(root.get("category").get("slug"), slug);
    }

    /** Filter by water type: "freshwater" | "saltwater" | "brackish" */
    public static Specification<Product> hasWaterType(String waterType) {
        if (waterType == null || waterType.isBlank() || waterType.equals("all")) return null;
        return (root, query, cb) ->
                cb.equal(root.get("waterType"), waterType);
    }

    /** Price range filter */
    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        if (min == null && max == null) return null;
        return (root, query, cb) -> {
            if (min != null && max != null)
                return cb.between(root.get("price"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("price"), min);
            return cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    /** Compose all non-null specs with AND */
    @SafeVarargs
    public static Specification<Product> compose(Specification<Product>... specs) {
        Specification<Product> result = Specification.where(null);
        for (Specification<Product> spec : specs) {
            if (spec != null) result = result.and(spec);
        }
        return result;
    }
}