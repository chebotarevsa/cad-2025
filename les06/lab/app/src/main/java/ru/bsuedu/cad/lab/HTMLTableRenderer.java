package ru.bsuedu.cad.lab;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Primary
@RequiredArgsConstructor
@Component
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider productProvider;

    @Override
    public void render() {
        StringBuilder table = new StringBuilder();
        table.append("<tr>").append("""
                <th>product_id</th>
                <th>name</th>
                <th>description</th>
                <th>category_id</th>
                <th>price</th>
                <th>stock_quantity</th>
                <th>image_url</th>
                <th>created_at</th>
                <th>updated_at</th>
                """).append("</tr>");
        productProvider.getProducts().forEach(product -> {
            table.append("<tr>")
                    .append("<td>").append(product.getProductId()).append("</td>")
                    .append("<td>").append(product.getName()).append("</td>")
                    .append("<td>").append(product.getDescription()).append("</td>")
                    .append("<td>").append(product.getCategoryId()).append("</td>")
                    .append("<td>").append(product.getPrice()).append("</td>")
                    .append("<td>").append(product.getStockQuantity()).append("</td>")
                    .append("<td>").append(product.getImageUrl()).append("</td>")
                    .append("<td>").append(product.getCreatedAt()).append("</td>")
                    .append("<td>").append(product.getUpdatedAt()).append("</td>")
                    .append("</tr>");
        });

        try (FileWriter fileWriter = new FileWriter("htmlrender.html")) {
            fileWriter.append("""
                    <html>
                    <head>
                    <meta charset="utf-8">
                    <title>Product table</title>
                    </head>
                    <body>
                    <table>
                    """ + table +
                    """
                    </table>
                    </body>
                    </html>
                    """);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
