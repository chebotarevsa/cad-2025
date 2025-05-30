package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ProductProvider {
    public List<Product> getProducts() throws NumberFormatException, IOException, ParseException; 
}
