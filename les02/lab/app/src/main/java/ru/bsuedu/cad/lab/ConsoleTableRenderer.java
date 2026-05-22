package ru.bsuedu.cad.lab;

import java.util.List;

public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        String format = "| %-3s | %-28s | %-42s | %-8s | %-8s | %-5s |%n";
        String border = "+-----+------------------------------+--------------------------------------------+----------+----------+-------+%n";

        System.out.printf(border);
        System.out.printf(format, "ID", "Name", "Description", "Category", "Price", "Stock");
        System.out.printf(border);
        for (Product product : products) {
            System.out.printf(format,
                    product.productId,
                    cut(product.name, 28),
                    cut(product.description, 42),
                    product.categoryId,
                    product.price,
                    product.stockQuantity);
        }
        System.out.printf(border);
    }

    private String cut(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }
}
