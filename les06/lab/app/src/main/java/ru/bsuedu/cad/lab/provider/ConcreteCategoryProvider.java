package ru.bsuedu.cad.lab.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    @Value("${category.file.name}")
    private String fileName;

    @Override
    public List<Category> getCategories() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new RuntimeException("Файл категорий не найден: " + fileName);
        }

        List<Category> categories = new ArrayList<>();
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            scanner.nextLine(); // пропустить заголовок
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                Category category = new Category(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2]
                );
                categories.add(category);
            }
        }
        return categories;
    }
}
