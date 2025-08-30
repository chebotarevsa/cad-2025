package ru.bsuedu.cad.lab;

public class Category {
    public int productId;
    public String name;
    public String description;

    public Category(int productId, String name, String description)
    {
            this.productId = productId;
            this.name = name;
            this. description = description;
    }

    @Override
    public String toString() {

        return "ProductId:" + productId + "\t Name:" + name + "\t Description:" + description;
    }
}
