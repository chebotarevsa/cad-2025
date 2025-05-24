package ru.bsuedu.cad.lab;

import java.util.List;

abstract public class ConcreteProvider<T> implements Provider<T>{
    public List<T> getItems() {
        final List<T> productsList = parser.parse(reader.read(path));
        return productsList;
    }

    final private String path;
    final private Reader reader;
    final private Parser<T> parser;

    public ConcreteProvider(Reader reader, Parser<T> parser, String path) {
        this.reader = reader;
        this.parser = parser;
        this.path = path;
    }
}
