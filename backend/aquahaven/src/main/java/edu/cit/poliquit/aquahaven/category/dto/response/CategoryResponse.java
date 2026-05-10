package edu.cit.poliquit.aquahaven.category.dto.response;

import edu.cit.poliquit.aquahaven.category.entity.Category;

public class CategoryResponse {

    private Long   id;
    private String name;
    private String slug;
    private String icon;
    private int    sortOrder;

    private CategoryResponse() {}

    public static CategoryResponse from(Category category) {
        CategoryResponse r = new CategoryResponse();
        r.id        = category.getId();
        r.name      = category.getName();
        r.slug      = category.getSlug();
        r.icon      = category.getIcon();
        r.sortOrder = category.getSortOrder();
        return r;
    }

    public Long   getId()        { return id; }
    public String getName()      { return name; }
    public String getSlug()      { return slug; }
    public String getIcon()      { return icon; }
    public int    getSortOrder() { return sortOrder; }
}