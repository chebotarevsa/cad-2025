package ru.bsu.cad.lab.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.bsu.cad.lab.model.Product;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductCSVParser implements Parser<Product> {
    @Override
    public List<Product> parse(String content) {
        List<Product> products = new ArrayList<>();
        try (StringReader reader = new StringReader(content);
             org.apache.commons.csv.CSVParser csvParser = new org.apache.commons.csv.CSVParser(
                 reader, 
                 CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                Product product = new Product();
                product.setProductId(Long.parseLong(record.get("product_id")));
                product.setName(record.get("name"));
                product.setDescription(record.get("description"));
                product.setCategoryId(Integer.parseInt(record.get("category_id")));
                product.setPrice(new BigDecimal(record.get("price")));
                product.setStockQuantity(Integer.parseInt(record.get("stock_quantity")));
                product.setImageUrl(record.get("image_url"));
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                product.setCreatedAt(dateFormat.parse(record.get("created_at")));
                product.setUpdatedAt(dateFormat.parse(record.get("updated_at")));
                
                products.add(product);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to parse CSV content", e);
        }
        return products;
    }
}