package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParser implements Parser {

    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String[] lines = data.split("\\r?\\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            try {
                products.add(new Product(
                    Long.parseLong(parts[0]),
                    parts[1],
                    parts[2],
                    Integer.parseInt(parts[3]),
                    new BigDecimal(parts[4]),
                    Integer.parseInt(parts[5]),
                    parts[6],
                    fmt.parse(parts[7]),
                    fmt.parse(parts[8])
                ));
            } catch (Exception e) {
                throw new RuntimeException("Ошибка разбора строки: " + line, e);
            }
        }
        return products;
    }
}
