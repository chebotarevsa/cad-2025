package org.example;

import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("category.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 3) {
                    int categoryId = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String description = data[2].trim();
                    categories.add(new Category(categoryId, name, description));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }
}