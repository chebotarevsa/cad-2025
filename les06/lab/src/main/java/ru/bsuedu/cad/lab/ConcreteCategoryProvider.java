package ru.bsuedu.cad.lab;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConcreteCategoryProvider {

    @Value("${category.file.name}")
    private String fileName;

    public List<Category> loadCategories() {
        List<Category> categories = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            int id = 1;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;
                categories.add(new Category(id++, line.trim()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения категорий", e);
        }
        return categories;
    }
}