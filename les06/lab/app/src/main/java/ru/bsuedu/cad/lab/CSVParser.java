package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CSVParser implements Parser {
    @Override
    public List<Product> parse(String value) {
        List<Product> products = new ArrayList<>();
        String[] lines = value.replace("\uFEFF", "").split("\\R");

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }
            String[] columns = lines[i].split(",");
            products.add(new Product(
                    Long.parseLong(columns[0]),
                    columns[1],
                    columns[2],
                    Integer.parseInt(columns[3]),
                    new BigDecimal(columns[4]),
                    Integer.parseInt(columns[5]),
                    columns[6],
                    Date.valueOf(columns[7]),
                    Date.valueOf(columns[8])
            ));
        }

        return products;
    }
}
