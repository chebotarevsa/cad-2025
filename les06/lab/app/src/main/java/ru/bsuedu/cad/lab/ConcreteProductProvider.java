package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//"Main" - класс, точка входа в приложение

@Component("productProvider")
public class ConcreteProductProvider implements Provider<Product> {
    private Reader reader;
    private Parser<Product> parser;

    @Autowired
    public ConcreteProductProvider(@Qualifier("productReader")Reader reader, Parser<Product> parser) { // Этот конструктор должен быть
        this.reader = reader;
        this.parser = parser;
    }
    
    // public List<Product> getProducts() throws NumberFormatException, ParseException, IOException{
    //     List<Product> productsList = parser.parse(reader.read());

    //     return productsList;
    // }

    @Override
    public Reader getReader() {
        return this.reader;
    }

    @Override
    public Parser<Product> getParser() {
        return this.parser;
    }
}
