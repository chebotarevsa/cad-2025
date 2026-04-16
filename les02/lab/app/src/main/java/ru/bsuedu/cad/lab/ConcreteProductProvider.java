package ru.bsuedu.cad.lab;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;

    @Override
    public List<Product> getProducts() {
        return parser.parse(reader.read());
    }
}
