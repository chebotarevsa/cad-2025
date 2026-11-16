package lab1;

import lab1.model.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider provider;

    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        try (PrintWriter out = new PrintWriter(new FileWriter("products.html"))) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><meta charset=\"UTF-8\"><title>Products</title></head><body>");
            out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"4\">");
            out.println("<tr><th>ID</th><th>Name</th><th>Description</th><th>Price</th></tr>");

            for (Product p : products) {
                out.printf(
                        "<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>%n",
                        p.getProductId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice()
                );
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка записи HTML-файла", e);
        }

        System.out.println("HTML-таблица записана в файл products.html");
    }
}
