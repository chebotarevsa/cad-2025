package ru.bsu.cad.lab.model;

public class Category {
    private int categoryId;
    private String name;
    private String description;

    // Геттеры и сеттеры
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}