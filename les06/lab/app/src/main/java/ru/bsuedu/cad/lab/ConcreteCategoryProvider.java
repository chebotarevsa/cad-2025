package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConcreteCategoryProvider {

    private final Reader reader;

    public ConcreteCategoryProvider(@Qualifier("categoryFileReader") Reader reader) {
        this.reader = reader;
    }

    public List<Category> getCategories() {
        return reader.read().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Category(
                            Long.parseLong(parts[0].trim()),
                            parts[1].trim(),
                            parts[2].trim()
                    );
                })
                .toList();
    }
}