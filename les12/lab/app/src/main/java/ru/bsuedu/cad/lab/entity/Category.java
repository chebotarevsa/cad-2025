package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIES")
public class Category {

    @Id
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public Category() {}

    public Category(Integer categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
