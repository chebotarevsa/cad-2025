package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {
    @Value("#{environment['categories.file']}")
    private String fileName;

    @Override
    public List<Category> getCategories() {
        String csv = readFile();
        List<Category> categories = new ArrayList<>();
        String[] lines = csv.replace("\uFEFF", "").split("\\R");

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }
            String[] columns = lines[i].split(",", 3);
            categories.add(new Category(
                    Long.parseLong(columns[0]),
                    columns[1],
                    columns[2]
            ));
        }

        return categories;
    }

    private String readFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Resource not found: " + fileName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + fileName, e);
        }
    }
}
