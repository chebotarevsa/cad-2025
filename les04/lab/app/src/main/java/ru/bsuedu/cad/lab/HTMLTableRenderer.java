package ru.bsuedu.cad.lab;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component("htmlRenderer")
public class HTMLTableRenderer implements Renderer {
    private ProductProvider provider;

    @Autowired    
    public HTMLTableRenderer(ProductProvider productProvider){
        this.provider = productProvider;
    }

    @Override
    public void render() throws IOException, NumberFormatException, ParseException {
        List<Product> products = provider.getProducts();

        try (FileWriter writer = new FileWriter("src/main/resources/products.html")) {
            writer.write("<html><head><meta charset=\"UTF-8\"><title>Products Table</title></head><body>");
            writer.write("<table border='1' style='border-collapse: collapse;'>");
            writer.write("<thead><tr>"
                + "<th>ID</th><th>Name</th><th>Description</th><th>Category</th>"
                + "<th>Price</th><th>Stock Quantity</th><th>Image URL</th><th>Created At</th><th>Updated At</th>"
                + "</tr></thead><tbody>");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Product p : products) {
                writer.write("<tr>"
                    + "<td>" + p.product_id + "</td>"
                    + "<td>" + escapeHtml(p.name) + "</td>"
                    + "<td>" + escapeHtml(p.description) + "</td>"
                    + "<td>" + p.category_id + "</td>"
                    + "<td>" + p.price + "</td>"
                    + "<td>" + p.stock_quantity + "</td>"
                    + "<td><a href='" + p.image_url + "'>Image</a></td>"
                    + "<td>" + sdf.format(p.created_at.getTime()) + "</td>"
                    + "<td>" + sdf.format(p.updated_at.getTime()) + "</td>"
                    + "</tr>");
            }

            writer.write("</tbody></table></body></html>");
        }

        System.out.println("HTML-File products.html was created.");
    }

    // Чтобы избежать проблем с HTML, простая функция замены спецсимволов
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}