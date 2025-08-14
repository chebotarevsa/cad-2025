package ru.bsuedu.cad.lab;

import java.util.ArrayList;
import java.util.List;

public class CSVParser implements Parser {
    @Override
    public List<Product> parse(List<String> lines) {
        List<Product> products = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String name = parts[0].trim();
                String category = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                products.add(new Product(name, category, price));
            }
        }
        return products;
    }
}