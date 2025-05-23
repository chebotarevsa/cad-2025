package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;

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
        return parser.parse(reader.readLines());
    }
}
