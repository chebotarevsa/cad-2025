package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("categoryProvider")
public class ConcreteCategoryProvider implements Provider<Category>{
    final private Reader reader;
    final private Parser<Category> parser;

    public ConcreteCategoryProvider(@Qualifier("categoryReader")Reader reader, Parser<Category> parser) {
        this.reader = reader;
        this.parser = parser;
     }

    @Override
    public Reader getReader() {
        return this.reader;
    }

    @Override
    public Parser<Category> getParser() {
        return this.parser;
    }
}