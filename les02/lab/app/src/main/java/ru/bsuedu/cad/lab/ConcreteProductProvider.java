package ru.bsuedu.cad.lab;

import java.util.List;

public class ConcreteProductProvider implements ProductProvider{

    @Override
    public List<Product> getProducts() {
        final List<Product> productsList = parser.parse(reader.read());
        return productsList;
    }

    final private Reader reader;
    final private Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    
}