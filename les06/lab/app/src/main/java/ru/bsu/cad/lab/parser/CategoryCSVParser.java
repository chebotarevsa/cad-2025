package ru.bsu.cad.lab.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.bsu.cad.lab.model.Category;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryCSVParser implements Parser<Category> {
    @Override
    public List<Category> parse(String content) {
        List<Category> categories = new ArrayList<>();
        try (StringReader reader = new StringReader(content);
             org.apache.commons.csv.CSVParser csvParser = new org.apache.commons.csv.CSVParser(
                 reader, 
                 CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                Category category = new Category();
                category.setCategoryId(Integer.parseInt(record.get("category_id")));
                category.setName(record.get("name"));
                category.setDescription(record.get("description"));
                categories.add(category);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV content", e);
        }
        return categories;
    }
}
