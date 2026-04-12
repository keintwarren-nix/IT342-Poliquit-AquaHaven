package edu.cit.poliquit.aquahaven.dto;

import edu.cit.poliquit.aquahaven.entity.Category;

public class CategoryDto {

    private Long id;
    private String slug;
    private String name;
    private String icon;
    private Integer sortOrder;

    public static CategoryDto from(Category c) {
        CategoryDto dto = new CategoryDto();
        dto.id        = c.getId();
        dto.slug      = c.getSlug();
        dto.name      = c.getName();
        dto.icon      = c.getIcon();
        dto.sortOrder = c.getSortOrder();
        return dto;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public Long    getId()        { return id; }
    public String  getSlug()     { return slug; }
    public String  getName()     { return name; }
    public String  getIcon()     { return icon; }
    public Integer getSortOrder(){ return sortOrder; }
}