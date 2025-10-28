package ru.bsuedu.cad.lab;

public class Category {
    public int id;
    public String name;
    public String description;

    public Category(int category_id, String name, String description)
    {
        this.id = category_id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
        }   
}