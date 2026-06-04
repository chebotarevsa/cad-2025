package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider provider;

    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n")
            .append("<title>Товары зоомагазина</title>\n")
            .append("<style>table{border-collapse:collapse}th,td{border:1px solid #333;padding:6px 10px}th{background:#4a90d9;color:#fff}</style>\n")
            .append("</head>\n<body>\n<h2>Товары зоомагазина</h2>\n<table>\n<tr>")
            .append("<th>ID</th><th>Название</th><th>Описание</th><th>Категория</th>")
            .append("<th>Цена</th><th>Склад</th><th>URL</th><th>Создан</th><th>Обновлён</th></tr>\n");

        for (Product p : products) {
            html.append("<tr>")
                .append("<td>").append(p.productId).append("</td>")
                .append("<td>").append(p.name).append("</td>")
                .append("<td>").append(p.description).append("</td>")
                .append("<td>").append(p.categoryId).append("</td>")
                .append("<td>").append(p.price.toPlainString()).append("</td>")
                .append("<td>").append(p.stockQuantity).append("</td>")
                .append("<td>").append(p.imageUrl).append("</td>")
                .append("<td>").append(fmt.format(p.createdAt)).append("</td>")
                .append("<td>").append(fmt.format(p.updatedAt)).append("</td>")
                .append("</tr>\n");
        }
        html.append("</table>\n</body>\n</html>");

        try {
            Path out = Path.of("output.html");
            Files.writeString(out, html.toString(), StandardCharsets.UTF_8);
            System.out.println("HTML-таблица сохранена: " + out.toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка записи HTML-файла", e);
        }
    }
}
