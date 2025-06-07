package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class ConcreteProductProvider implements ProductProvider {
    private Reader reader;
    private Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) { // Этот конструктор должен быть
        this.reader = reader;
        this.parser = parser;
    }
    
    public List<Product> getProducts() throws NumberFormatException, ParseException, IOException{
        List<Product> productsList = parser.parse(reader.read());

        return productsList;
    }
}
