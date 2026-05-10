package edu.cit.poliquit.aquahaven.product.entity;

import edu.cit.poliquit.aquahaven.category.entity.Category;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "products_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private int stock = 0;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "water_type", length = 20)
    private String waterType;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long          getId()                               { return id; }
    public void          setId(Long id)                        { this.id = id; }
    public String        getName()                             { return name; }
    public void          setName(String name)                  { this.name = name; }
    public String        getDescription()                      { return description; }
    public void          setDescription(String description)    { this.description = description; }
    public BigDecimal    getPrice()                            { return price; }
    public void          setPrice(BigDecimal price)            { this.price = price; }
    public String        getImageUrl()                         { return imageUrl; }
    public void          setImageUrl(String imageUrl)          { this.imageUrl = imageUrl; }
    public int           getStock()                            { return stock; }
    public void          setStock(int stock)                   { this.stock = stock; }
    public boolean       isActive()                            { return active; }
    public void          setActive(boolean active)             { this.active = active; }
    public Category      getCategory()                         { return category; }
    public void          setCategory(Category category)        { this.category = category; }
    public String        getWaterType()                        { return waterType; }
    public void          setWaterType(String waterType)        { this.waterType = waterType; }
    public LocalDateTime getCreatedAt()                        { return createdAt; }
    public void          setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}