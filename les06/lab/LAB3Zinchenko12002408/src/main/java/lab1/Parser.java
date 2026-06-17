package lab1;

import lab1.model.Product;

import java.util.List;

public interface Parser {
    List<Product> parse(String data);
}
