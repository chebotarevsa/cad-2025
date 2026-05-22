package ru.bsuedu.cad.lab;

public class Category {
    public long categoryId;
    public String name;
    public String description;

    public Category(long categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }
}
