package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("ProductProvider")
public class ConcreteProductProvider extends ConcreteProvider<Product>{

    public ConcreteProductProvider(Reader reader, Parser<Product> parser, FilePathProvider pathProvider) {
        super(reader, parser, pathProvider.getFileNameProduct());
    }
}
