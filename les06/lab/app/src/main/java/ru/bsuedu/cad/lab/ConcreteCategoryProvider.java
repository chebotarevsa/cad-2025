package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    @Value("${category.file}")
    private String filename;

    @Override
    public List<Category> getCategories() {
        try {
            ClassPathResource resource = new ClassPathResource(filename);
            InputStream is = resource.getInputStream();
            String data = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return parse(data);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать файл: " + filename, e);
        }
    }

    private List<Category> parse(String data) {
        List<Category> categories = new ArrayList<>();
        String[] lines = data.split("\\r?\\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            // limit=3 чтобы запятые в описании не ломали парсинг
            String[] parts = line.split(",", 3);
            categories.add(new Category(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2]
            ));
        }
        return categories;
    }
}
