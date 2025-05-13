package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {
    private final ProductProvider provider;

    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        try (FileWriter writer = new FileWriter("products.html")) {
            writer.write("<html><head><meta charset=\"UTF-8\"></head><body>\n");
            writer.write("<table border='1'>\n");
            writer.write("<tr><th>ID</th><th>Name</th><th>Description</th><th>Category</th><th>Price</th><th>Stock</th><th>Image URL</th></tr>\n");

            for (Product p : products) {
                writer.write(String.format(
                    "<tr><td>%d</td><td>%s</td><td>%s</td><td>%d</td><td>%s</td><td>%d</td><td><a href='%s'>%s</a></td></tr>\n",
                    p.getProductId(), p.getName(), p.getDescription(), p.getCategoryId(),
                    p.getPrice(), p.getStockQuantity(), p.getImageUrl(), "Ссылка"
                ));
            }

            writer.write("</table></body></html>");
            System.out.println("HTML-таблица успешно сохранена в products.html");
        } catch (Exception e) {
            System.err.println("Ошибка при создании HTML-файла: " + e.getMessage());
        }
    }
}
