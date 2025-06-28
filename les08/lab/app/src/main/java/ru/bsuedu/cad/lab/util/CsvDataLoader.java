package ru.bsuedu.cad.lab.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvDataLoader {
    private static final Logger logger = LoggerFactory.getLogger(CsvDataLoader.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<String[]> loadCsvData(String filename) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                data.add(line.split(","));
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла: {}", filename, e);
        }
        return data;
    }

    public LocalDateTime parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER).atStartOfDay();
    }

    public BigDecimal parsePrice(String priceStr) {
        return new BigDecimal(priceStr);
    }
}