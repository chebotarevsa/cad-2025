package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CSVParser implements Parser {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Product> parse(String content) {
        List<Product> products = new ArrayList<>();
        String[] lines = content.split("\\r?\\n");

        for (int i = 1; i < lines.length; i++) { // Skip header
            String[] values = lines[i].split(",");
            if (values.length >= 9) {
                try {
                    Product product = new Product();
                    product.setProductId(Long.parseLong(values[0].trim()));
                    product.setName(values[1].trim());
                    product.setDescription(values[2].trim());
                    product.setCategoryId(Integer.parseInt(values[3].trim()));
                    product.setPrice(new BigDecimal(values[4].trim()));
                    product.setStockQuantity(Integer.parseInt(values[5].trim()));
                    product.setImageUrl(values[6].trim());
                    product.setCreatedAt(parseDate(values[7].trim()));
                    product.setUpdatedAt(parseDate(values[8].trim()));
                    products.add(product);
                } catch (Exception e) {
                    System.err.println("Ошибка при парсинге строки " + i + ": " + e.getMessage());
                }
            }
        }

        return products;
    }

    @Override
    public List<Category> parseCategories(String content) {
        List<Category> categories = new ArrayList<>();
        String[] lines = content.split("\\r?\\n");

        for (int i = 1; i < lines.length; i++) { // Skip header
            String[] values = lines[i].split(",");
            if (values.length >= 3) {
                try {
                    Category category = new Category();
                    category.setCategoryId(Integer.parseInt(values[0].trim()));
                    category.setName(values[1].trim());
                    category.setDescription(values[2].trim());
                    categories.add(category);
                } catch (Exception e) {
                    System.err.println("Ошибка при парсинге строки " + i + ": " + e.getMessage());
                }
            }
        }

        return categories;
    }

    private Date parseDate(String dateString) throws ParseException {
        return dateString.isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    }
}