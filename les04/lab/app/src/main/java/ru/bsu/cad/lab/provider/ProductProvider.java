package ru.bsu.cad.lab.provider;

import ru.bsu.cad.lab.model.Product;
import java.util.List;

public interface ProductProvider {
    List<Product> getProducts();
}
