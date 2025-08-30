package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Component("HTML")
public class HTMLTableRenderer implements Renderer {

    private final Provider<Product> provider;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HTMLTableRenderer(Provider <Product> productProvider) {
        this.provider = productProvider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getEntitees();
        String htmlContent = buildHtmlContent(products);
        
        try {
            Path outputPath = Paths.get("src/main/resources/products.html");
            Files.createDirectories(outputPath.getParent());
            
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
                writer.write(htmlContent);
                System.out.println("HTML file successfully created at: " + outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error creating HTML file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildHtmlContent(List<Product> products) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html>\n")
            .append("<head>\n")
            .append("<meta charset=\"UTF-8\">\n")
            .append("    <title>Products Table</title>\n")
            .append("    <style>\n")
            .append("        table { border-collapse: collapse; width: 100%; }\n")
            .append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n")
            .append("        th { background-color: #f2f2f2; }\n")
            .append("        img { max-width: 100px; max-height: 100px; }\n")
            .append("    </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("    <h1>Products List</h1>\n")
            .append(buildTable(products))
            .append("</body>\n")
            .append("</html>");
        
        return html.toString();
    }

    private String buildTable(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return "<p>No products available</p>";
        }

        StringBuilder table = new StringBuilder();
        table.append("<table>\n")
             .append("    <thead>\n")
             .append("        <tr>\n")
             .append("            <th>ID</th>\n")
             .append("            <th>Name</th>\n")
             .append("            <th>Description</th>\n")
             .append("            <th>Category ID</th>\n")
             .append("            <th>Price</th>\n")
             .append("            <th>Stock</th>\n")
             .append("            <th>Image</th>\n")
             .append("            <th>Created Date</th>\n")
             .append("            <th>Updated Date</th>\n")
             .append("        </tr>\n")
             .append("    </thead>\n")
             .append("    <tbody>\n");

        for (Product product : products) {
            table.append("        <tr>\n")
                 .append("            <td>").append(product.productId).append("</td>\n")
                 .append("            <td>").append(escapeHtml(product.name)).append("</td>\n")
                 .append("            <td>").append(escapeHtml(product.description)).append("</td>\n")
                 .append("            <td>").append(product.categoryId).append("</td>\n")
                 .append("            <td>").append(formatPrice(product.price)).append("</td>\n")
                 .append("            <td>").append(product.stockQuantity).append("</td>\n")
                 .append("            <td>").append(buildImageTag(product.imageUrl)).append("</td>\n")
                 .append("            <td>").append(calendarToString(product.createdAt)).append("</td>\n")
                 .append("            <td>").append(calendarToString(product.updatedAt)).append("</td>\n")
                 .append("        </tr>\n");
        }

        table.append("    </tbody>\n")
             .append("</table>");
        
        return table.toString();
    }

    private String formatPrice(BigDecimal price) {
        return price != null ? String.format("%.2f", price) : "0.00";
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private String buildImageTag(String url) {
        if (url == null || url.isEmpty()) {
            return "No image";
        }
        return String.format("<img src=\"%s\" alt=\"Product image\">", escapeHtml(url));
    }
    public String calendarToString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

}