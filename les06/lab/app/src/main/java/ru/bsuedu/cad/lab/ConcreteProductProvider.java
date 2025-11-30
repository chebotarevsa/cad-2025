package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("productProvider")
public class ConcreteProductProvider extends ConcreteProvider <Product>{

    public ConcreteProductProvider(Reader reader, Parser parser) {
        super(reader, parser);
    }
    
    
    

    
}
