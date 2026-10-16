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

        System.out.println("+------+----------------------+----------------------+--------+------------+--------+");
        System.out.printf("| %-4s | %-20s | %-20s | %-6s | %-10s | %-6s |%n",
                "ID", "Название", "Описание", "Кат.", "Цена", "Остаток");
        System.out.println("+------+----------------------+----------------------+--------+------------+--------+");

        for (Product p : products) {
            System.out.printf("| %-4d | %-20s | %-20s | %-6d | %-10s | %-6d |%n",
                    p.getProductId(),
                    truncate(p.getName(), 20),
                    truncate(p.getDescription(), 20),
                    p.getCategoryId(),
                    p.getPrice().toPlainString(),
                    p.getStockQuantity());
        }

        System.out.println("+------+----------------------+----------------------+--------+------------+--------+");
        System.out.println("Всего товаров: " + products.size());
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}