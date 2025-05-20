package ru.bsuedu.cad.lab;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("htmlTableRenderer")
public class HTMLTableRenderer implements Renderer {

    private ProductProvider provider;

    @Autowired
    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        File file = new File("src/main/resources/output.html");
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Products Table</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 20px;
                        }
                        h1 {
                            color: #2c3e50;
                            text-align: center;
                            margin-bottom: 30px;
                        }
                        table {
                            border-collapse: collapse;
                            width: 100%;
                            margin-bottom: 20px;
                            box-shadow: 0 2px 3px rgba(0,0,0,0.1);
                        }
                        th, td {
                            border: 1px solid #ddd;
                            padding: 12px;
                            text-align: left;
                        }
                        th {
                            background-color: #3498db;
                            color: white;
                            font-weight: bold;
                            position: sticky;
                            top: 0;
                        }
                        tr:nth-child(even) {
                            background-color: #f2f2f2;
                        }
                        tr:hover {
                            background-color: #e3f2fd;
                        }
                        .numeric {
                            text-align: right;
                        }
                        .center {
                            text-align: center;
                        }
                        footer {
                            text-align: center;
                            margin-top: 30px;
                            color: #7f8c8d;
                            font-size: 0.9em;
                        }
                    </style>
                </head>
                <body>
                    <h1>Products Information</h1>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Stock</th>
                                <th>Image URL</th>
                                <th>Created</th>
                                <th>Updated</th>
                            </tr>
                        </thead>
                        <tbody>
                """);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Product product : products) {
            htmlBuilder.append("<tr>")
                    .append("<td class=\"center\">").append(product.productId).append("</td>")
                    .append("<td>").append(escapeHtml(product.name)).append("</td>")
                    .append("<td>").append(escapeHtml(product.description)).append("</td>")
                    .append("<td class=\"center\">").append(product.categoryId).append("</td>")
                    .append("<td class=\"numeric\">").append(String.format("%.2f", product.price)).append("</td>")
                    .append("<td class=\"center\">").append(product.stockQuantity).append("</td>")
                    .append("<td>").append(escapeHtml(product.imageUrl)).append("</td>")
                    .append("<td>").append(dateFormat.format(product.createdAt)).append("</td>")
                    .append("<td>").append(dateFormat.format(product.updatedAt)).append("</td>")
                    .append("</tr>");
        }

        htmlBuilder.append("""
                        </tbody>
                    </table>
                    <footer>
                        Generated on """ + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date()) + """
                        | Total products: """ + products.size() + """
                    </footer>
                </body>
                </html>
                """);

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println(htmlBuilder.toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}