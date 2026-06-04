package ru.bsuedu.cad.lab.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.Product;
import ru.bsuedu.cad.lab.ProductProvider;
import ru.bsuedu.cad.lab.Renderer;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    private static final String[] HEADERS = {
        "ID", "Название", "Описание", "Кат.", "Цена", "Склад", "Создан"
    };

    private final ProductProvider provider;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Value("#{environment.getProperty('products.html.output')}")
    private String outputFileName;

    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = Objects.requireNonNull(provider, "provider");
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        String html = buildHtml(products);
        Path outputPath = Path.of(outputFileName).toAbsolutePath();

        try {
            Files.writeString(outputPath, html);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write HTML file: " + outputPath, e);
        }

        System.out.println("HTML-таблица сохранена: " + outputPath);
        System.out.println("Количество товаров: " + products.size());
    }

    private String buildHtml(List<Product> products) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"ru\">\n<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>Каталог зоотоваров</title>\n");
        html.append("  <style>\n");
        html.append("    table { border-collapse: collapse; width: 100%; }\n");
        html.append("    th, td { border: 1px solid #333; padding: 8px; text-align: left; }\n");
        html.append("    th { background-color: #f0f0f0; }\n");
        html.append("  </style>\n");
        html.append("</head>\n<body>\n");
        html.append("  <h1>Каталог зоотоваров</h1>\n");
        html.append("  <table>\n    <thead>\n      <tr>\n");

        for (String header : HEADERS) {
            html.append("        <th>").append(escape(header)).append("</th>\n");
        }

        html.append("      </tr>\n    </thead>\n    <tbody>\n");

        for (Product product : products) {
            html.append("      <tr>\n");
            html.append(cell(product.getProductId()));
            html.append(cell(product.getName()));
            html.append(cell(product.getDescription()));
            html.append(cell(product.getCategoryId()));
            html.append(cell(product.getPrice().toPlainString()));
            html.append(cell(product.getStockQuantity()));
            html.append(cell(dateFormat.format(product.getCreatedAt())));
            html.append("      </tr>\n");
        }

        html.append("    </tbody>\n  </table>\n</body>\n</html>\n");
        return html.toString();
    }

    private String cell(Object value) {
        return "        <td>" + escape(String.valueOf(value)) + "</td>\n";
    }

    private String escape(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
