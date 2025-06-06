package ru.bsuedu.cad.lab.service;

import java.util.List;
import ru.bsuedu.cad.lab.model.Product;

public interface Parser {
    List<Product> parse(List<String> lines);
}
