package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcreteProductProvider implements ProductProvider {

    @Autowired
    private Reader reader;

    @Autowired
    private Parser parser;

    @Override
    public List<Product> getProducts() {
        List<String> lines = reader.readAllLines();
        return parser.parse(lines);
    }
}