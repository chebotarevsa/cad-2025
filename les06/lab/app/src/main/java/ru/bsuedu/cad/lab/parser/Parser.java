package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

public interface Parser {
    List<Product> parse(String content);
}
