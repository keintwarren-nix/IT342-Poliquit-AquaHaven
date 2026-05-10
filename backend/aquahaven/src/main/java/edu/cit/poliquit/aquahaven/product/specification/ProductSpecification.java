package edu.cit.poliquit.aquahaven.product.specification;

import edu.cit.poliquit.aquahaven.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    private ProductSpecification() {}

    public static Specification<Product> isAvailable() {
        return (root, query, cb) ->
                cb.and(
                        cb.isTrue(root.get("active")),
                        cb.greaterThan(root.get("stock"), 0)
                );
    }

    public static Specification<Product> nameOrDescriptionContains(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        String pattern = "%" + keyword.toLowerCase() + "%";
        return (root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                );
    }

    public static Specification<Product> inCategorySlug(String slug) {
        if (slug == null || slug.isBlank() || slug.equals("all")) return null;
        return (root, query, cb) ->
                cb.equal(root.get("category").get("slug"), slug);
    }

    public static Specification<Product> hasWaterType(String waterType) {
        if (waterType == null || waterType.isBlank() || waterType.equals("all")) return null;
        return (root, query, cb) ->
                cb.equal(root.get("waterType"), waterType);
    }

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

    @SafeVarargs
    public static Specification<Product> compose(Specification<Product>... specs) {
        Specification<Product> result = Specification.where(null);
        for (Specification<Product> spec : specs) {
            if (spec != null) result = result.and(spec);
        }
        return result;
    }
}