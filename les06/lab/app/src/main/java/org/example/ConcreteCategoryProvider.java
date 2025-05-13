package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    private final String categoryFile;
    private final CSVParser csvParser;

    @Autowired
    public ConcreteCategoryProvider(@Value("${category.file}") String categoryFile, CSVParser csvParser) {
        this.categoryFile = categoryFile;
        this.csvParser = csvParser;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            var resource = new ClassPathResource(categoryFile);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                List<String[]> lines = csvParser.parse(reader);
                for (String[] fields : lines.subList(1, lines.size())) { // Пропускаем заголовок
                    int id = Integer.parseInt(fields[0]);
                    String name = fields[1];
                    String description = fields[2];
                    categories.add(new Category(id, name, description));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения категорий", e);
        }

        return categories;
    }
}
