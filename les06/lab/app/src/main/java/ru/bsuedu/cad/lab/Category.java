package ru.bsuedu.cad.lab;

public class Category {
    public int product_id;
    public String name;
    public String description;

    public Category(int product_id, String name, String description)
    {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
    }
}
