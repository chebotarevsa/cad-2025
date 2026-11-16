package ru.bsuedu.cad.lab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// @Component  
public class HTMLTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Товары</title></head><body>");
        html.append("<table border='1'>");
        html.append("<tr><th>ID</th><th>Название</th><th>Цена</th><th>Категория</th></tr>");
        for (Product p : products) {
            html.append(String.format("<tr><td>%d</td><td>%s</td><td>%.2f</td><td>%s</td></tr>",
                    p.getId(), p.getName(), p.getPrice(), p.getCategory()));
        }
        html.append("</table></body></html>");

        try (FileWriter fw = new FileWriter("products.html")) {
            fw.write(html.toString());
            System.out.println("HTML-отчёт сохранён в файл products.html");
        } catch (IOException e) {
            System.err.println("Ошибка записи HTML: " + e.getMessage());
        }
    }
}