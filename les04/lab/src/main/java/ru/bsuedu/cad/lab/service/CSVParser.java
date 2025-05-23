package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;

import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParser implements Parser {
    @Override
    public List<Product> parse(List<String> lines) {
        List<Product> result = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = line.split(",");
            Product p = new Product();
            p.productId = Integer.parseInt(tokens[0]);
            p.name = tokens[1];
            p.description = tokens[2];
            p.categoryId = Integer.parseInt(tokens[3]);
            p.price = Double.parseDouble(tokens[4]);
            p.stockQuantity = Integer.parseInt(tokens[5]);
            p.imageUrl = tokens[6];
            p.createdAt = tokens[7];
            p.updatedAt = tokens[8];
            result.add(p);
        }
        return result;
    }
}
