package edu.cit.poliquit.aquahaven.product.dto.request;

import java.math.BigDecimal;

public class ProductSearchRequest {

    private String     keyword;
    private String     categorySlug;
    private String     waterType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String     sortBy  = "createdAt";
    private String     sortDir = "desc";
    private int        page    = 0;
    private int        size    = 20;

    public String     getKeyword()              { return keyword; }
    public void       setKeyword(String k)      { this.keyword = k; }
    public String     getCategorySlug()         { return categorySlug; }
    public void       setCategorySlug(String s) { this.categorySlug = s; }
    public String     getWaterType()            { return waterType; }
    public void       setWaterType(String w)    { this.waterType = w; }
    public BigDecimal getMinPrice()             { return minPrice; }
    public void       setMinPrice(BigDecimal v) { this.minPrice = v; }
    public BigDecimal getMaxPrice()             { return maxPrice; }
    public void       setMaxPrice(BigDecimal v) { this.maxPrice = v; }
    public String     getSortBy()               { return sortBy; }
    public void       setSortBy(String s)       { this.sortBy = s; }
    public String     getSortDir()              { return sortDir; }
    public void       setSortDir(String s)      { this.sortDir = s; }
    public int        getPage()                 { return page; }
    public void       setPage(int p)            { this.page = p; }
    public int        getSize()                 { return size; }
    public void       setSize(int s)            { this.size = s; }
}