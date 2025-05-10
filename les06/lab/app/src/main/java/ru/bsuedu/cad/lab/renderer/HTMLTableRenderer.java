package ru.bsuedu.cad.lab.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import ru.bsuedu.cad.lab.renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
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

        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"><title>Продукты</title></head><body>");
        html.append("<h1>Список товаров</h1>");
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr>")
            .append("<th>ID</th>")
            .append("<th>Название</th>")
            .append("<th>Описание</th>")
            .append("<th>Категория</th>")
            .append("<th>Цена</th>")
            .append("<th>Остаток</th>")
            .append("<th>Картинка</th>")
            .append("<th>Создан</th>")
            .append("<th>Обновлён</th>")
            .append("</tr>");

        for (Product product : products) {
            html.append("<tr>")
                .append("<td>").append(product.getProductId()).append("</td>")
                .append("<td>").append(product.getName()).append("</td>")
                .append("<td>").append(product.getDescription()).append("</td>")
                .append("<td>").append(product.getCategoryId()).append("</td>")
                .append("<td>").append(product.getPrice()).append("</td>")
                .append("<td>").append(product.getStockQuantity()).append("</td>")
                .append("<td><img src='").append(product.getImageUrl()).append("' width='50'/></td>")
                .append("<td>").append(product.getCreatedAt()).append("</td>")
                .append("<td>").append(product.getUpdatedAt()).append("</td>")
                .append("</tr>");
        }

        html.append("</table></body></html>");

        try (FileWriter writer = new FileWriter("output.html")) {
            writer.write(html.toString());
            System.out.println("HTML-файл успешно создан: output.html");
        } catch (IOException e) {
            System.out.println("Ошибка записи в HTML-файл: " + e.getMessage());
        }
    }
}
