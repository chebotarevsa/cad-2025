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

    @Autowired
    public ConcreteCategoryProvider(@Value("${category.file}") String categoryFile) {
        this.categoryFile = categoryFile;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(categoryFile);
            try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] fields = line.split(",");
                    if (fields.length >= 3) {
                        int id = Integer.parseInt(fields[0].trim());
                        String name = fields[1].trim();
                        String description = fields[2].trim();
                        categories.add(new Category(id, name, description));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения категорий", e);
        }
        return categories;
    }
}

