package ru.bsuedu.cad.lab;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcreteCategoryProvider implements CategoryProvider {

    private final Reader reader;
    private final Parser parser;

    @Value("${application.filename.cat}")
    private String path;

    @Override
    public List<Category> getCategories() {
        return parser.parseCategories(reader.read(path));
    }
}
