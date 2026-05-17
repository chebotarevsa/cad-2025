package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    @Autowired
    private ProductProvider productProvider;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void render() throws Exception {
        List<Product> products = productProvider.getProducts();
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<table border='1'>");
        html.append("<tr>");
        html.append("<th>ID</th><th>Name</th><th>Description</th><th>Category</th><th>Price</th><th>Stock</th><th>Image</th><th>Created</th><th>Updated</th>");
        html.append("</tr>");

        for (Product p : products) {
            html.append("<tr>");
            html.append("<td>").append(p.getProductId()).append("</td>");
            html.append("<td>").append(p.getName()).append("</td>");
            html.append("<td>").append(p.getDescription()).append("</td>");
            html.append("<td>").append(p.getCategoryId()).append("</td>");
            html.append("<td>").append(p.getPrice()).append("</td>");
            html.append("<td>").append(p.getStockQuantity()).append("</td>");
            html.append("<td>").append(p.getImageUrl()).append("</td>");
            html.append("<td>").append(dateFormat.format(p.getCreatedAt())).append("</td>");
            html.append("<td>").append(dateFormat.format(p.getUpdatedAt())).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");

        try (PrintWriter out = new PrintWriter(new FileWriter("product_table.html"))) {
            out.println(html.toString());
        }
        System.out.println("HTML table written to product_table.html");
    }
}