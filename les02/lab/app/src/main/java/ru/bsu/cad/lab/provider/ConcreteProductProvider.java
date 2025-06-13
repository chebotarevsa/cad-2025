package ru.bsu.cad.lab.provider;

import org.springframework.stereotype.Component;
import ru.bsu.cad.lab.model.Product;
import ru.bsu.cad.lab.parser.Parser;
import ru.bsu.cad.lab.parser.ProductCSVParser;
import ru.bsu.cad.lab.reader.Reader;

import java.util.List;

@Component
public class ConcreteProductProvider implements ProductProvider {
    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, ProductCSVParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        String content = reader.read();
        return parser.parse(content);
    }
}