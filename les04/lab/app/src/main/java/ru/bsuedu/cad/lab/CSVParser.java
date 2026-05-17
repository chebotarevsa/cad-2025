package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CSVParser implements Parser {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Product> parse(String data) throws Exception {
        List<Product> products = new ArrayList<>();
        String[] lines = data.split("\n");
        if (lines.length < 2) {
            return products;
        }
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] fields = line.split(",");
            if (fields.length < 9) continue;

            long productId = Long.parseLong(fields[0].trim());
            String name = fields[1].trim();
            String description = fields[2].trim();
            int categoryId = Integer.parseInt(fields[3].trim());
            BigDecimal price = new BigDecimal(fields[4].trim());
            int stockQuantity = Integer.parseInt(fields[5].trim());
            String imageUrl = fields[6].trim();
            Date createdAt = dateFormat.parse(fields[7].trim());
            Date updatedAt = dateFormat.parse(fields[8].trim());

            products.add(new Product(productId, name, description, categoryId,
                    price, stockQuantity, imageUrl, createdAt, updatedAt));
        }
        return products;
    }
}