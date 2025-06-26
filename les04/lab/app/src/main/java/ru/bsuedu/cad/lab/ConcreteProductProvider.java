package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("provider")
public class ConcreteProductProvider implements ProductProvider{
    final private Reader reader;
    final private Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    public List<Product> getProducts()
    {
        final List<Product> productsList = parser.parse(reader.read());
        return productsList;
    }

    
}
