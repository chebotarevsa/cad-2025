package ru.bsuedu.cad.lab.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.bsuedu.cad.lab.Parser;
import ru.bsuedu.cad.lab.Product;

public class CSVParser implements Parser {

    private static final int EXPECTED_FIELD_COUNT = 9;

    @Override
    public List<Product> parse(String content) {
        String[] lines = content.strip().split("\\R");
        if (lines.length < 2) {
            return List.of();
        }

        List<Product> products = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }
            products.add(parseLine(line));
        }
        return products;
    }

    private Product parseLine(String line) {
        String[] fields = line.split(",", EXPECTED_FIELD_COUNT);
        if (fields.length != EXPECTED_FIELD_COUNT) {
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }

        return new Product(
                Long.parseLong(fields[0].trim()),
                fields[1].trim(),
                fields[2].trim(),
                Integer.parseInt(fields[3].trim()),
                new BigDecimal(fields[4].trim()),
                Integer.parseInt(fields[5].trim()),
                fields[6].trim(),
                parseDate(fields[7].trim()),
                parseDate(fields[8].trim()));
    }

    private Date parseDate(String value) {
        LocalDate localDate = LocalDate.parse(value);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
