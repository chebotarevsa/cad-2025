package ru.bsuedu.cad.lab;

import java.util.List;

public interface Parser {
    List<Product> parse(String content);
    List<Category> parseCategories(String content);
}
