package ru.bsuedu.cad.lab;

import java.util.List;

public class ConcreteProductProvider implements ProductProvider {
private final Reader reader;
private final Parser<Product> parser;

    public ConcreteProductProvider(Reader reader, Parser<Product> parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        List<String> lines = reader.readAllLines();
        return parser.parse(lines);
    }
}