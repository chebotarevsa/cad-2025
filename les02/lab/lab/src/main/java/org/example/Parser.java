package org.example;
import java.util.List;

public interface Parser {
    List<Product> parse(String data);
}