package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(
            @Qualifier("resourceFileReader") Reader reader,
            Parser parser
    ) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        return parser.parse(reader.read());
    }
}