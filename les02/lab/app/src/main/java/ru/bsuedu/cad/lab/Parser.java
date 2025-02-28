package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface Parser {
    public List<Product> parse(String textString) throws IOException, NumberFormatException, ParseException;
}
