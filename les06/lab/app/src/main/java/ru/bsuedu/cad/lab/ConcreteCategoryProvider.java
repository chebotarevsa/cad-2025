package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("categoryProvider")
public class ConcreteCategoryProvider extends ConcreteProvider <Category>{

    public ConcreteCategoryProvider(Reader reader, Parser parser) {
        super(reader, parser);
    }
    
    
}
