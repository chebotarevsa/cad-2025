package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CSVParser implements Parser {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();
        String[] lines = data.split("\\r?\\n");
        if (lines.length < 2) return products;

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] fields = line.split(",", -1);
            try {
                Product p = new Product();
                p.setProductId(Long.parseLong(fields[0].trim()));
                p.setName(fields[1].trim());
                p.setDescription(fields[2].trim());
                p.setCategoryId(Integer.parseInt(fields[3].trim()));
                p.setPrice(new BigDecimal(fields[4].trim()));
                p.setStockQuantity(Integer.parseInt(fields[5].trim()));
                p.setImageUrl(fields[6].trim());
                p.setCreatedAt(parseDate(fields[7].trim()));
                p.setUpdatedAt(parseDate(fields[8].trim()));
                products.add(p);
            } catch (Exception e) {
                System.err.println("Ошибка строки " + i + ": " + e.getMessage());
            }
        }
        return products;
    }

    private Date parseDate(String s) {
        try { return dateFormat.parse(s); }
        catch (Exception e) { return new Date(0); }
    }
}