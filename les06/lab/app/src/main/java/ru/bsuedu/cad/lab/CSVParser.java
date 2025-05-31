package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

@Component
public class CSVParser implements Parser {
    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();
        String[] lines = data.split("\n");

        // Пропускаем заголовок
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty()) {
                String[] cols = line.split(",");
                try {
                    Product product = new Product(
                        Long.parseLong(cols[0]),
                        cols[1],
                        cols[2],
                        Integer.parseInt(cols[3]),
                        new BigDecimal(cols[4]),
                        Integer.parseInt(cols[5]),
                        cols[6],
                        new SimpleDateFormat("yyyy-MM-dd").parse(cols[7]),
                        new SimpleDateFormat("yyyy-MM-dd").parse(cols[8])
                    );
                    products.add(product);
                } catch (Exception e) {
                    System.err.println("Ошибка при парсинге строки: " + line);
                }
            }
        }

        return products;
    }
}