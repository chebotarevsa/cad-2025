package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {

    private final ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        int[] widths = {4, 35, 35, 8, 10, 6, 42, 12, 12};
        String[] headers = {"ID", "Название", "Описание", "Кат.", "Цена", "Склад", "URL изображения", "Создан", "Обновлён"};

        printLine(widths);
        printRow(headers, widths);
        printLine(widths);

        for (Product p : products) {
            String[] row = {
                String.valueOf(p.productId), p.name, p.description,
                String.valueOf(p.categoryId), p.price.toPlainString(),
                String.valueOf(p.stockQuantity), p.imageUrl,
                fmt.format(p.createdAt), fmt.format(p.updatedAt)
            };
            printRow(row, widths);
        }
        printLine(widths);
    }

    private void printLine(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) sb.append("-".repeat(w + 2)).append("+");
        System.out.println(sb);
    }

    private void printRow(String[] cells, int[] widths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < cells.length; i++) {
            String cell = cells[i] != null ? cells[i] : "";
            if (cell.length() > widths[i]) cell = cell.substring(0, widths[i] - 3) + "...";
            sb.append(" ").append(cell).append(" ".repeat(widths[i] - cell.length())).append(" |");
        }
        System.out.println(sb);
    }
}
