package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider provider;
    private final String filename;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public HTMLTableRenderer(ProductProvider provider, String filename)
    {
        this.provider = provider;
        this.filename = filename;
    }

    @Override
    public void render() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            List<Product> products = provider.getProducts();

            if (products.isEmpty()) {
                System.out.println("Товары не найдены");
                return;
            }

            writer.write("<html>\n");
            writer.write("\t<head>\n");
            writer.write("\t\t<meta charset=\"utf-8\">\n");
            writer.write("\t\t<title>Products</title>\n");
            writer.write("\t\t<style>\n");
            writer.write("\t\t\ttable {\n");
            writer.write("\t\t\t\tborder: 1px solid #ccc;\n");
            writer.write("\t\t\t\tborder-spacing: 3px;\n\t\t\t}\n");
            writer.write("\t\t\ttd, th {\n");
            writer.write("\t\t\t\tborder: solid 1px #ccc;\n\t\t\t}\n");
            writer.write("\t\t\t.collapsed {\n");
            writer.write("\t\t\t\tborder-collapse: collapse;\n\t\t\t}\n");
            writer.write("\t\t</style>\n");
            writer.write("\t</head>\n");
            writer.write("\t<body>\n");
            writer.write("\t\t<table class=\"collapsed\">\n");
            writer.write("\t\t\t<thead>\n");
            writer.write("\t\t\t\t<tr>\n");
            writer.write("\t\t\t\t\t<th>ID</th>\n");
            writer.write("\t\t\t\t\t<th>Название</th>\n");
            writer.write("\t\t\t\t\t<th>Описание</th>\n");
            writer.write("\t\t\t\t\t<th>Категория</th>\n");
            writer.write("\t\t\t\t\t<th>Цена</th>\n");
            writer.write("\t\t\t\t\t<th>Кол-во</th>\n");
            writer.write("\t\t\t\t\t<th>Изображение</th>\n");
            writer.write("\t\t\t\t\t<th>Создан</th>\n");
            writer.write("\t\t\t\t\t<th>Обновлён</th>\n");
            writer.write("\t\t\t\t</tr>\n\t\t\t</thead>\n");
            writer.write("\t\t\t<tbody>\n");
            for (Product product : products) {
                writer.write("\t\t\t\t<tr>\n");
                writer.write(String.format("\t\t\t\t\t<td>%d</td>\n", product.getProductId()));
                writer.write(String.format("\t\t\t\t\t<td>%s</td>\n", product.getName()));
                writer.write(String.format("\t\t\t\t\t<td>%s</td>\n", product.getDescription()));
                writer.write(String.format("\t\t\t\t\t<td>%d</td>\n", product.getCategoryId()));
                writer.write(String.format("\t\t\t\t\t<td>%s</td>\n", product.getPrice()));
                writer.write(String.format("\t\t\t\t\t<td>%d</td>\n", product.getStockQuantity()));
                writer.write(String.format("\t\t\t\t\t<td>%s</td>\n", product.getImageUrl()));
                writer.write(String.format("\t\t\t\t\t<td>%s</td>\n", formatDate(product.getCreatedAt())));
                writer.write(String.format("\t\t\t\t\t<td>%s</td>\n", formatDate(product.getUpdatedAt())));
                writer.write("\t\t\t\t</tr>\n");
            }
            writer.write("\t\t\t</tbody>\n");
            writer.write("\t\t</table>\n");
            writer.write("\t</body>\n");
            writer.write("</html>\n");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки ресурса: ", e);
        }
    }

    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "";
    }
}
