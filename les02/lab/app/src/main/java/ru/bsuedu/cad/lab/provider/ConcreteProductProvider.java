package ru.bsuedu.cad.lab.provider;

import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.parser.Parser;
import ru.bsuedu.cad.lab.reader.Reader;

import java.util.List;

public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        String data = reader.read();
        return parser.parse(data);
    }
}
