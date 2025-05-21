package ru.bsuedu.cad.lab.service;

import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

public class ConsoleTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products) {
        System.out.printf(
                "%-5s %-25s %-30s %-8s %-10s %-10s %-35s %-12s %-12s%n",
                "ID", "Name", "Description", "CatID", "Price", "Stock", "Image URL", "Created", "Updated"
        );

        for (Product p : products) {
            System.out.printf(
                    "%-5d %-25s %-30s %-8d %-10.2f %-10d %-35s %-12s %-12s%n",
                    p.productId, p.name, p.description, p.categoryId, p.price,
                    p.stockQuantity, p.imageUrl, p.createdAt, p.updatedAt
            );
        }
    }
}
