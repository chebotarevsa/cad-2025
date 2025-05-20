package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("CategoryProvider")
public class ConcreteCategoryProvider extends ConcreteProvider<Category>{

    public ConcreteCategoryProvider(Reader reader, Parser<Category> parser, FilePathProvider pathProvider) {
        super(reader, parser, pathProvider.getFileNameCategory());
    }
}
