package ru.bsuedu.cad.lab.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products) {
        try (PrintWriter out = new PrintWriter("products.html", StandardCharsets.UTF_8)) {
            out.println("<html><body><table border='1'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Description</th><th>Category</th><th>Price</th><th>Stock</th><th>Image</th><th>Created</th><th>Updated</th></tr>");
            for (Product p : products) {
                out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%d</td><td>%.2f</td><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>%n",
                    p.productId, p.name, p.description, p.categoryId, p.price, p.stockQuantity,
                    p.imageUrl, p.createdAt, p.updatedAt);
            }
            out.println("</table></body></html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
