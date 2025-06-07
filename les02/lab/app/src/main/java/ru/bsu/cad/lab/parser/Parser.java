package ru.bsu.cad.lab.parser;

import ru.bsu.cad.lab.model.Product;
import java.util.List;

public interface Parser {
    List<Product> parse(String content);
}
