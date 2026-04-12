
package edu.cit.poliquit.aquahaven.specification;

import java.math.BigDecimal;

/**
 * Plain value object that carries all optional filter parameters
 * from the HTTP query string to the Specification builder.
 * Keeps the controller and service clean — no long parameter lists.
 */
public class ProductFilterCriteria {

    private String search;
    private String categorySlug;
    private String environment;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private boolean inStockOnly;
    private String sortBy   = "name";   // "name" | "price" | "createdAt"
    private String sortDir  = "asc";    // "asc"  | "desc"
    private int page        = 0;
    private int size        = 12;

    // ── Getters & Setters ──────────────────────────────────────────────────
    public String getSearch()                       { return search; }
    public void setSearch(String search)            { this.search = search; }

    public String getCategorySlug()                 { return categorySlug; }
    public void setCategorySlug(String s)           { this.categorySlug = s; }

    public String getEnvironment()                  { return environment; }
    public void setEnvironment(String e)            { this.environment = e; }

    public BigDecimal getMinPrice()                 { return minPrice; }
    public void setMinPrice(BigDecimal min)         { this.minPrice = min; }

    public BigDecimal getMaxPrice()                 { return maxPrice; }
    public void setMaxPrice(BigDecimal max)         { this.maxPrice = max; }

    public boolean isInStockOnly()                  { return inStockOnly; }
    public void setInStockOnly(boolean b)           { this.inStockOnly = b; }

    public String getSortBy()                       { return sortBy; }
    public void setSortBy(String sortBy)            { this.sortBy = sortBy; }

    public String getSortDir()                      { return sortDir; }
    public void setSortDir(String sortDir)          { this.sortDir = sortDir; }

    public int getPage()                            { return page; }
    public void setPage(int page)                   { this.page = page; }

    public int getSize()                            { return size; }
    public void setSize(int size)                   { this.size = size; }
}