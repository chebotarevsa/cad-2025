package ru.bsu.cad.lab.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.bsu.cad.lab.model.Category;
import ru.bsu.cad.lab.parser.Parser;
import ru.bsu.cad.lab.reader.Reader; // Используем интерфейс
import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {
    private final Reader reader; // Изменяем тип на Reader
    private final Parser<Category> parser;

    public ConcreteCategoryProvider(
            @Qualifier("categoryReader") Reader reader, // Исправляем тип
            Parser<Category> parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Category> getCategories() {
        String content = reader.read();
        return parser.parse(content);
    }
}