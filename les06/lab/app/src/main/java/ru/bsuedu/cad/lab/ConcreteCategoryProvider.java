package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    private final Reader reader;
    private final Parser parser;

    public ConcreteCategoryProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Category> getCategories() {
        String content = reader.read();
        return parser.parseCategories(content);
    }
}