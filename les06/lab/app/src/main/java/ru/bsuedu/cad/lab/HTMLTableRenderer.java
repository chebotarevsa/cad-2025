package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {
    private final ProductProvider provider;

    @Value("#{environment['products.html']}")
    private String fileName;

    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Products</title>\n</head>\n<body>\n");
            writer.write("<table border=\"1\">\n");
            writer.write("<tr><th>ID</th><th>Name</th><th>Description</th><th>Category</th><th>Price</th><th>Stock</th></tr>\n");

            for (Product product : products) {
                writer.write("<tr>");
                writer.write("<td>" + product.productId + "</td>");
                writer.write("<td>" + product.name + "</td>");
                writer.write("<td>" + product.description + "</td>");
                writer.write("<td>" + product.categoryId + "</td>");
                writer.write("<td>" + product.price + "</td>");
                writer.write("<td>" + product.stockQuantity + "</td>");
                writer.write("</tr>\n");
            }

            writer.write("</table>\n</body>\n</html>\n");
            System.out.println("HTML table saved to " + fileName);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write HTML file", e);
        }
    }
}
