package edu.cit.poliquit.aquahaven.dto;

import edu.cit.poliquit.aquahaven.entity.Category;
import edu.cit.poliquit.aquahaven.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable DTOs built with the Builder pattern —
 * consistent with how AuthResponse is already structured.
 */
public class ProductDto {

    // ── Category DTO ────────────────────────────────────────────────────────────

    public static class CategoryResponse {
        private final Long   id;
        private final String name;
        private final String slug;
        private final String icon;
        private final int    sortOrder;

        private CategoryResponse(Builder b) {
            this.id        = b.id;
            this.name      = b.name;
            this.slug      = b.slug;
            this.icon      = b.icon;
            this.sortOrder = b.sortOrder;
        }

        public static CategoryResponse from(Category c) {
            return new Builder()
                    .id(c.getId())
                    .name(c.getName())
                    .slug(c.getSlug())
                    .icon(c.getIcon())
                    .sortOrder(c.getSortOrder())
                    .build();
        }

        public Long   getId()        { return id; }
        public String getName()      { return name; }
        public String getSlug()      { return slug; }
        public String getIcon()      { return icon; }
        public int    getSortOrder() { return sortOrder; }

        public static class Builder {
            private Long id; private String name, slug, icon; private int sortOrder;
            public Builder id(Long v)        { id = v;        return this; }
            public Builder name(String v)    { name = v;      return this; }
            public Builder slug(String v)    { slug = v;      return this; }
            public Builder icon(String v)    { icon = v;      return this; }
            public Builder sortOrder(int v)  { sortOrder = v; return this; }
            public CategoryResponse build()  { return new CategoryResponse(this); }
        }
    }

    // ── Product DTO ─────────────────────────────────────────────────────────────

    public static class ProductResponse {
        private final Long          id;
        private final String        name;
        private final String        description;
        private final BigDecimal    price;
        private final String        imageUrl;
        private final int           stock;
        private final String        waterType;
        private final LocalDateTime createdAt;
        private final CategoryResponse category;

        private ProductResponse(Builder b) {
            this.id          = b.id;
            this.name        = b.name;
            this.description = b.description;
            this.price       = b.price;
            this.imageUrl    = b.imageUrl;
            this.stock       = b.stock;
            this.waterType   = b.waterType;
            this.createdAt   = b.createdAt;
            this.category    = b.category;
        }

        /** Convenience factory from entity */
        public static ProductResponse from(Product p) {
            return new Builder()
                    .id(p.getId())
                    .name(p.getName())
                    .description(p.getDescription())
                    .price(p.getPrice())
                    .imageUrl(p.getImageUrl())
                    .stock(p.getStock())
                    .waterType(p.getWaterType())
                    .createdAt(p.getCreatedAt())
                    .category(CategoryResponse.from(p.getCategory()))
                    .build();
        }

        public Long             getId()          { return id; }
        public String           getName()        { return name; }
        public String           getDescription() { return description; }
        public BigDecimal       getPrice()       { return price; }
        public String           getImageUrl()    { return imageUrl; }
        public int              getStock()       { return stock; }
        public String           getWaterType()   { return waterType; }
        public LocalDateTime    getCreatedAt()   { return createdAt; }
        public CategoryResponse getCategory()    { return category; }

        public static class Builder {
            private Long id; private String name, description, imageUrl, waterType;
            private BigDecimal price; private int stock; private LocalDateTime createdAt;
            private CategoryResponse category;

            public Builder id(Long v)                  { id = v;          return this; }
            public Builder name(String v)              { name = v;        return this; }
            public Builder description(String v)       { description = v; return this; }
            public Builder price(BigDecimal v)         { price = v;       return this; }
            public Builder imageUrl(String v)          { imageUrl = v;    return this; }
            public Builder stock(int v)                { stock = v;       return this; }
            public Builder waterType(String v)         { waterType = v;   return this; }
            public Builder createdAt(LocalDateTime v)  { createdAt = v;   return this; }
            public Builder category(CategoryResponse v){ category = v;    return this; }
            public ProductResponse build()             { return new ProductResponse(this); }
        }
    }

    // ── Search / Filter request (inbound) ──────────────────────────────────────

    public static class ProductSearchRequest {
        private String keyword;
        private String categorySlug;
        private String waterType;
        private java.math.BigDecimal minPrice;
        private java.math.BigDecimal maxPrice;
        private String sortBy   = "createdAt";    // name | price | createdAt
        private String sortDir  = "desc";         // asc | desc
        private int    page     = 0;
        private int    size     = 20;

        public String getKeyword()       { return keyword; }
        public void setKeyword(String v) { keyword = v; }

        public String getCategorySlug()       { return categorySlug; }
        public void setCategorySlug(String v) { categorySlug = v; }

        public String getWaterType()       { return waterType; }
        public void setWaterType(String v) { waterType = v; }

        public java.math.BigDecimal getMinPrice()       { return minPrice; }
        public void setMinPrice(java.math.BigDecimal v) { minPrice = v; }

        public java.math.BigDecimal getMaxPrice()       { return maxPrice; }
        public void setMaxPrice(java.math.BigDecimal v) { maxPrice = v; }

        public String getSortBy()       { return sortBy; }
        public void setSortBy(String v) { sortBy = v; }

        public String getSortDir()       { return sortDir; }
        public void setSortDir(String v) { sortDir = v; }

        public int getPage()       { return page; }
        public void setPage(int v) { page = v; }

        public int getSize()       { return size; }
        public void setSize(int v) { size = Math.min(v, 100); }
    }
}