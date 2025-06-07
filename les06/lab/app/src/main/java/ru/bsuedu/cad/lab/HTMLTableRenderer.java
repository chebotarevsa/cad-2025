package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {
    private final ProductProvider provider;

    @Autowired
    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        System.out.println("<table>");
        System.out.println("<tr><th>ID</th><th>Name</th><th>Description</th><th>Category</th><th>Price</th><th>Stock</th><th>Image URL</th></tr>");
        for (Product product : products) {
            System.out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%d</td><td>%s</td><td>%d</td><td>%s</td></tr>%n",
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryId(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getImageUrl());
        }
        System.out.println("</table>");
    }
}
