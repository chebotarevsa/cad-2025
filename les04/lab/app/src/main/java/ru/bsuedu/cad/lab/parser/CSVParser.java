package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CSVParser implements Parser {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Product> parse(String content) {
        List<Product> products = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new StringReader(content))) {
            String[] header = reader.readNext(); // skip header
            String[] line;
            while ((line = reader.readNext()) != null) {
                Product p = new Product();
                p.setProductId(Long.parseLong(line[0]));
                p.setName(line[1]);
                p.setDescription(line[2]);
                p.setCategoryId(Integer.parseInt(line[3]));
                p.setPrice(new BigDecimal(line[4]));
                p.setStockQuantity(Integer.parseInt(line[5]));
                p.setImageUrl(line[6]);
                p.setCreatedAt(dateFormat.parse(line[7]));
                p.setUpdatedAt(dateFormat.parse(line[8]));
                products.add(p);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге CSV", e);
        }

        return products;
    }
}
