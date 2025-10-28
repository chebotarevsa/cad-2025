package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//"Main" - класс, точка входа в приложение

@Component("provider")
public class ConcreteProductProvider implements ProductProvider {
    private Reader reader;
    private Parser parser;

    @Autowired
    public ConcreteProductProvider(Reader reader, Parser parser) { // Этот конструктор должен быть
        this.reader = reader;
        this.parser = parser;
    }
    
    public List<Product> getProducts() throws NumberFormatException, ParseException, IOException{
        List<Product> productsList = parser.parse(reader.read());

        return productsList;
    }
}
