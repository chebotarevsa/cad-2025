package ru.bsuedu.cad.lab;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;

    @Value("${application.filename}")
    private String path;

    @Override
    public List<Product> getProducts() {
        return parser.parse(reader.read(path));
    }
}
