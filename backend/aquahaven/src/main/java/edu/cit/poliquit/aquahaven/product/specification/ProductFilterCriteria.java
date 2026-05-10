package edu.cit.poliquit.aquahaven.product.specification;

import java.math.BigDecimal;

public class ProductFilterCriteria {

    private String     search;
    private String     categorySlug;
    private String     environment;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private boolean    inStockOnly;
    private String     sortBy  = "name";
    private String     sortDir = "asc";
    private int        page    = 0;
    private int        size    = 12;

    public String     getSearch()              { return search; }
    public void       setSearch(String s)      { this.search = s; }
    public String     getCategorySlug()        { return categorySlug; }
    public void       setCategorySlug(String s){ this.categorySlug = s; }
    public String     getEnvironment()         { return environment; }
    public void       setEnvironment(String e) { this.environment = e; }
    public BigDecimal getMinPrice()            { return minPrice; }
    public void       setMinPrice(BigDecimal v){ this.minPrice = v; }
    public BigDecimal getMaxPrice()            { return maxPrice; }
    public void       setMaxPrice(BigDecimal v){ this.maxPrice = v; }
    public boolean    isInStockOnly()          { return inStockOnly; }
    public void       setInStockOnly(boolean b){ this.inStockOnly = b; }
    public String     getSortBy()              { return sortBy; }
    public void       setSortBy(String s)      { this.sortBy = s; }
    public String     getSortDir()             { return sortDir; }
    public void       setSortDir(String s)     { this.sortDir = s; }
    public int        getPage()                { return page; }
    public void       setPage(int p)           { this.page = p; }
    public int        getSize()                { return size; }
    public void       setSize(int s)           { this.size = s; }
}