package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConcreteProductProvider implements ProductProvider {

    @Autowired
    private Reader reader;

    @Autowired
    private Parser parser;

    @Override
    public List<Product> getProducts() throws Exception {
        String csvData = reader.read();
        return parser.parse(csvData);
    }
}