package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface Parser<T> {
    List<T> parse(String textString) throws IOException, NumberFormatException, ParseException;
}
