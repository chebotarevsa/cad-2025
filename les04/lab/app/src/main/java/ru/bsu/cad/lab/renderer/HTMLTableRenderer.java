package ru.bsu.cad.lab.renderer;

import org.springframework.stereotype.Component;
import ru.bsu.cad.lab.model.Product;
import ru.bsu.cad.lab.provider.ProductProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String html = generateHTML(products);

        Path outputPath = Paths.get("output/products.html");

        try {
            // Создаём директорию, если её нет
            Files.createDirectories(outputPath.getParent());
            // Записываем HTML в файл
            Files.writeString(outputPath, html);
            System.out.println("HTML-таблица сохранена в файл: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private String generateHTML(List<Product> products) {
        StringBuilder html = new StringBuilder();
        html.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Products Table</title>
                <style>
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <h1>Список товаров</h1>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Цена</th>
                        <th>Остаток</th>
                    </tr>
            """);

        for (Product product : products) {
            html.append(String.format("""
                <tr>
                    <td>%d</td>
                    <td>%s</td>
                    <td>%.2f</td>
                    <td>%d</td>
                </tr>
                """,
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
            ));
        }

        html.append("""
                </table>
            </body>
            </html>
            """);
        return html.toString();
    }
}
