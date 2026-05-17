package ru.bsuedu.cad.lab;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider productProvider;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ConsoleTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    @Override
    public void render() throws Exception {
        List<Product> products = productProvider.getProducts();
        if (products.isEmpty()) {
            System.out.println("No products to display.");
            return;
        }

        String format = "| %-5s | %-25s | %-35s | %-10s | %-10s | %-8s | %-30s | %-16s | %-16s |%n";
        String separator = "+-------+---------------------------+-------------------------------------+------------+------------+----------+--------------------------------+------------------+------------------+";

        System.out.println(separator);
        System.out.printf(format, "ID", "Name", "Description", "Category", "Price", "Stock", "Image", "Created", "Updated");
        System.out.println(separator);

        for (Product p : products) {
            System.out.printf(format,
                    p.getProductId(),
                    truncate(p.getName(), 25),
                    truncate(p.getDescription(), 35),
                    p.getCategoryId(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    truncate(p.getImageUrl(), 30),
                    dateFormat.format(p.getCreatedAt()),
                    dateFormat.format(p.getUpdatedAt()));
        }
        System.out.println(separator);
    }

    private String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) return value;
        return value.substring(0, maxLength - 3) + "...";
    }
}