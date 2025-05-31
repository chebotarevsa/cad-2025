package ru.bsuedu.cad.lab;

public class Category {
    private int categoryId;
    private String name;
    private String description;

    public Category(int categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    // геттеры и сеттеры
    public int getId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
