package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider provider;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        if (products.isEmpty()) {
            System.out.println("Товары не найдены.");
            return;
        }

        String headerFormat = "| %-6s | %-20s | %-30s | %-9s | %-10s | %-8s | %-20s | %-16s | %-15s |%n";
        String rowFormat = "| %-6d | %-20s | %-30s | %-9d | %-10.2f | %-8d | %-20s | %-16s | %-15s |%n";

        System.out.println("+----------------------------------------------------------------------------------------------------------------------------------------------------------------+");
        System.out.printf(headerFormat,
                "ID", "Название", "Описание", "Категория", "Цена", "Кол-во", "Изображение", "Создан", "Обновлён");
        System.out.println("+----------------------------------------------------------------------------------------------------------------------------------------------------------------+");

        for (Product product : products) {
            System.out.printf(rowFormat,
                    product.getProductId(),
                    shorten(product.getName(), 20),
                    shorten(product.getDescription(), 30),
                    product.getCategoryId(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    shortenUrl(product.getImageUrl()),
                    formatDate(product.getCreatedAt()),
                    formatDate(product.getUpdatedAt()));
        }

        System.out.println("+----------------------------------------------------------------------------------------------------------------------------------------------------------------+");
    }

    private String shorten(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    private String shortenUrl(String url) {
        if (url == null) return "";
        return url.length() > 20 ? url.substring(0, 17) + ".." : url;
    }

    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "";
    }
}