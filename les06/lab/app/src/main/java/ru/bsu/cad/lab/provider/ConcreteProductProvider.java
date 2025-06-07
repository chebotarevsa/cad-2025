package ru.bsu.cad.lab.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.bsu.cad.lab.model.Product;
import ru.bsu.cad.lab.parser.Parser;
import ru.bsu.cad.lab.reader.Reader; // Используем интерфейс
import java.util.List;

@Component
public class ConcreteProductProvider implements ProductProvider {
    private final Reader reader; // Изменяем тип на Reader
    private final Parser<Product> parser;

    public ConcreteProductProvider(
            @Qualifier("productReader") Reader reader, // Исправляем тип
            Parser<Product> parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        String content = reader.read();
        return parser.parse(content);
    }
}