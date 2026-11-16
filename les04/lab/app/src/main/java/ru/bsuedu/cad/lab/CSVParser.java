package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CSVParser implements Parser {

    @Override
    public List<Product> parse(List<String> lines) {
        return lines.stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(line -> {
                    String[] parts = line.split(",");

                    return new Product(
                            Long.parseLong(parts[0].trim()),
                            parts[1].trim(),
                            parts[2].trim(),
                            Double.parseDouble(parts[4].trim()),
                            Integer.parseInt(parts[5].trim())
                    );
                })
                .toList();
    }
}
