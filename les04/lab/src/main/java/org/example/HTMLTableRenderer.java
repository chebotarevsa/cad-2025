package org.example;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    @Autowired
    private ProductProvider productProvider;

    @Override
    public void render() {
        List<Product> products = productProvider.getProducts();

        StringBuilder html = new StringBuilder();
        html.append("<html><body><table border='1'>\n");
        html.append("<tr><th>ID</th><th>Name</th><th>Price</th></tr>\n");

        for (Product p : products) {
            html.append("<tr>");
            html.append("<td>").append(p.getProductId()).append("</td>");
            html.append("<td>").append(p.getName()).append("</td>");
            html.append("<td>").append(p.getPrice()).append("</td>");
            html.append("</tr>\n");
        }

        html.append("</table></body></html>");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("report.html"), StandardCharsets.UTF_8))) {
            writer.write(html.toString());
            System.out.println("HTML отчет создан: report.html");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}