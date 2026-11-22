package org.example;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSVParser implements Parser {

    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();

        try {
            String[] lines = data.split("\n");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");

                Product product = new Product(
                        Long.parseLong(values[0]),
                        values[1],
                        values[2],
                        Integer.parseInt(values[3]),
                        new BigDecimal(values[4]),
                        Integer.parseInt(values[5]),
                        values[6],
                        format.parse(values[7]),
                        format.parse(values[8])
                );

                products.add(product);
            }

        } catch (Exception e) {
            System.out.println("Ошибка парсинга CSV: " + e.getMessage());
        }

        return products;
    }
}