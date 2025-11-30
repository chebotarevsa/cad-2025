package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component("htmlRenderer")
public class HTMLTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body><table border='1'>");
        html.append("<tr><th>Name</th><th>Category</th><th>Price</th></tr>");
        for (Product product : products) {
            html.append(String.format("<tr><td>%s</td><td>%s</td><td>%.2f</td></tr>",
                    product.getName(), product.getCategory(), product.getPrice()));
        }
        html.append("</table></body></html>");

        try (FileWriter writer = new FileWriter("products.html")) {
            writer.write(html.toString());
            System.out.println("HTML table saved to products.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}