package ru.bsuedu.cad.lab.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.Parser;
import ru.bsuedu.cad.lab.Product;
import ru.bsuedu.cad.lab.ProductProvider;
import ru.bsuedu.cad.lab.Reader;

@Component
public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = Objects.requireNonNull(reader, "reader");
        this.parser = Objects.requireNonNull(parser, "parser");
    }

    @Override
    public List<Product> getProducts() {
        return parser.parse(reader.read());
    }
}
