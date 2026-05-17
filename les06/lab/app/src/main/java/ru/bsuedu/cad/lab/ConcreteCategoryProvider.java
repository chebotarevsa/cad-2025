package ru.bsuedu.cad.lab;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    @Value("${categories.file.name}")
    private String fileName;

    @Override
    public List<Category> getCategories() throws Exception {
        List<Category> categories = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(fileName);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                int categoryId = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                String description = fields[2].trim();
                categories.add(new Category(categoryId, name, description));
            }
        }
        return categories;
    }
}