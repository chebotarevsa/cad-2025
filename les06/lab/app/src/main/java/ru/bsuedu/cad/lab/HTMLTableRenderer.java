package ru.bsuedu.cad.lab;

// import org.springframework.context.annotation.Primary;
import java.io.FileWriter;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class HTMLTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {
        try (FileWriter writer = new FileWriter("product.html")) {

            writer.write("<!DOCTYPE html>");
            writer.write("<html><head><meta charset='UTF-8'><style>");
            writer.write("body { font-family: Arial, sans-serif; display: flex; justify-content: center; padding: 40px; background: #f5f5f5; }");
            writer.write("table { border-collapse: collapse; width: 70%; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }");
            writer.write("th { background-color: #4a6fa5; color: white; padding: 12px 16px; text-align: left; }");
            writer.write("td { padding: 10px 16px; border-bottom: 1px solid #e0e0e0; }");
            writer.write("tr:last-child td { border-bottom: none; }");
            writer.write("tr:nth-child(even) td { background-color: #f9f9f9; }");
            writer.write("tr:hover td { background-color: #eef2f8; }");
            writer.write("</style></head><body>");
            writer.write("<table>");
            writer.write("<tr><th>ID</th><th>Название</th><th>Цена</th><th>Остаток</th></tr>");

            for (Product p : products) {
                writer.write("<tr>");
                writer.write("<td>" + p.getId() + "</td>");
                writer.write("<td>" + p.getName() + "</td>");
                writer.write("<td>" + p.getPrice() + "</td>");
                writer.write("<td>" + p.getStockQuantity() + "</td>");
                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("</body></html>");

            System.out.println("HTML файл создан: product.html");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}