
package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;

import java.util.List;

@Component
public class ConcreteCategoryProvider {
    public List<Category> loadCategories() {
        return CSVParser.readCategories("/category.csv");
    }
}
