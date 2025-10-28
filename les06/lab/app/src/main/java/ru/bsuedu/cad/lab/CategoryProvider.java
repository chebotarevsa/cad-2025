package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CategoryProvider {
    public List<Category> getCategory() throws NumberFormatException, IOException, ParseException; 
}