package ru.bsuedu.cad.lab.service;

import ru.bsuedu.cad.lab.model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class CSVParser implements Parser {
    @Override
    public List<Product> parse(List<String> lines) {
        return lines.stream().map(line -> {
            String[] parts = line.split(",");
            var product = new Product();
            product.productId = Integer.parseInt(parts[0]);
            product.name = parts[1];
            product.description = parts[2];
            product.categoryId = Integer.parseInt(parts[3]);
            product.price = Double.parseDouble(parts[4]);
            product.stockQuantity = Integer.parseInt(parts[5]);
            product.imageUrl = parts[6];
            product.createdAt = parts[7];
            product.updatedAt = parts[8];
            return product;
        }).collect(Collectors.toList());
    }
}
