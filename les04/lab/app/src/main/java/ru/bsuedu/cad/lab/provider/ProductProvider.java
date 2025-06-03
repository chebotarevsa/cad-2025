package ru.bsuedu.cad.lab.provider;

import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

public interface ProductProvider {
    List<Product> getProducts();
}
