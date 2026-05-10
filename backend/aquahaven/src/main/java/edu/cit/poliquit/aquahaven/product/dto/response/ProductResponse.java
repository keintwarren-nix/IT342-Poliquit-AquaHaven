package edu.cit.poliquit.aquahaven.product.dto.response;

import edu.cit.poliquit.aquahaven.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long          id;
    private String        name;
    private String        description;
    private BigDecimal    price;
    private String        imageUrl;
    private int           stock;
    private String        waterType;
    private Long          categoryId;
    private String        categoryName;
    private String        categorySlug;
    private LocalDateTime createdAt;

    private ProductResponse() {}

    public static ProductResponse from(Product product) {
        ProductResponse r = new ProductResponse();
        r.id          = product.getId();
        r.name        = product.getName();
        r.description = product.getDescription();
        r.price       = product.getPrice();
        r.imageUrl    = product.getImageUrl();
        r.stock       = product.getStock();
        r.waterType   = product.getWaterType();
        r.createdAt   = product.getCreatedAt();
        if (product.getCategory() != null) {
            r.categoryId   = product.getCategory().getId();
            r.categoryName = product.getCategory().getName();
            r.categorySlug = product.getCategory().getSlug();
        }
        return r;
    }

    public Long          getId()           { return id; }
    public String        getName()         { return name; }
    public String        getDescription()  { return description; }
    public BigDecimal    getPrice()        { return price; }
    public String        getImageUrl()     { return imageUrl; }
    public int           getStock()        { return stock; }
    public String        getWaterType()    { return waterType; }
    public Long          getCategoryId()   { return categoryId; }
    public String        getCategoryName() { return categoryName; }
    public String        getCategorySlug() { return categorySlug; }
    public LocalDateTime getCreatedAt()    { return createdAt; }
}