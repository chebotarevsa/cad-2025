package ru.bsuedu.cad.lab;

import java.util.List;

public interface  Parser {
    public <T> List<T> parse(Class<T> clazz, String text);

}
