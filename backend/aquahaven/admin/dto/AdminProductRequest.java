package edu.cit.poliquit.aquahaven.admin.dto;

import java.math.BigDecimal;

public class AdminProductRequest {

    private String     name;
    private String     description;
    private BigDecimal price;
    private int        stock;
    private String     imageUrl;
    private String     waterType;
    private Long       categoryId;
    private boolean    active = true;

    public String     getName()                    { return name; }
    public void       setName(String name)         { this.name = name; }
    public String     getDescription()             { return description; }
    public void       setDescription(String d)     { this.description = d; }
    public BigDecimal getPrice()                   { return price; }
    public void       setPrice(BigDecimal price)   { this.price = price; }
    public int        getStock()                   { return stock; }
    public void       setStock(int stock)          { this.stock = stock; }
    public String     getImageUrl()                { return imageUrl; }
    public void       setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String     getWaterType()               { return waterType; }
    public void       setWaterType(String w)       { this.waterType = w; }
    public Long       getCategoryId()              { return categoryId; }
    public void       setCategoryId(Long id)       { this.categoryId = id; }
    public boolean    isActive()                   { return active; }
    public void       setActive(boolean active)    { this.active = active; }
}