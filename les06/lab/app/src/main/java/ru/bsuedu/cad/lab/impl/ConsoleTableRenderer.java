package ru.bsuedu.cad.lab.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.Product;
import ru.bsuedu.cad.lab.ProductProvider;
import ru.bsuedu.cad.lab.Renderer;

@Component("consoleTableRenderer")
public class ConsoleTableRenderer implements Renderer {

    private static final String[] HEADERS = {
        "ID", "Название", "Описание", "Кат.", "Цена", "Склад", "Создан"
    };

    private final ProductProvider provider;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = Objects.requireNonNull(provider, "provider");
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        List<String[]> rows = new ArrayList<>();
        for (Product product : products) {
            rows.add(new String[] {
                String.valueOf(product.getProductId()),
                product.getName(),
                product.getDescription(),
                String.valueOf(product.getCategoryId()),
                product.getPrice().toPlainString(),
                String.valueOf(product.getStockQuantity()),
                dateFormat.format(product.getCreatedAt())
            });
        }

        int[] widths = calculateWidths(rows);
        printBorder(widths);
        printRow(HEADERS, widths);
        printBorder(widths);
        for (String[] row : rows) {
            printRow(row, widths);
        }
        printBorder(widths);
    }

    private int[] calculateWidths(List<String[]> rows) {
        int[] widths = new int[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            widths[i] = HEADERS[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }
        return widths;
    }

    private void printBorder(int[] widths) {
        StringBuilder border = new StringBuilder("+");
        for (int width : widths) {
            border.append("-".repeat(width + 2)).append("+");
        }
        System.out.println(border);
    }

    private void printRow(String[] values, int[] widths) {
        StringBuilder row = new StringBuilder("|");
        for (int i = 0; i < values.length; i++) {
            row.append(" ").append(pad(values[i], widths[i])).append(" |");
        }
        System.out.println(row);
    }

    private String pad(String value, int width) {
        if (value.length() >= width) {
            return value;
        }
        return value + " ".repeat(width - value.length());
    }
}
