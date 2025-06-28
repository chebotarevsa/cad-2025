package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcreteProductProvider implements ProductProvider {
    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        String content = reader.read();
        return parser.parse(content);
    }
}
