
package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

@Component
public class ConcreteProductProvider {
    public List<Product> loadProducts() {
        return CSVParser.readProducts("/products.csv");
    }
}
