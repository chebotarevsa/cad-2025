package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider provider;

    @Autowired
    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        System.out.printf("%-10s %-30s %-50s %-12s %-10s %-10s %-60s%n", "ID", "Name", "Description", "Category", "Price", "Stock", "Image URL");
        for (Product product : products) {
            System.out.printf("%-10d %-30s %-50s %-12d %-10s %-10d %-60s%n",
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryId(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getImageUrl());
        }
    }
}