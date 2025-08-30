package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component
public class CategoryCSVParser extends CSVParser<Category> {

    @Override
    protected Category parseLine(String[] params) {
        return new Category(stringToInteger(params[0]), params[1], params[2]);
    }
}