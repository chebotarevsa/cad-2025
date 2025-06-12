package ru.bsu.cad.lab.provider;

import ru.bsu.cad.lab.model.Category;
import java.util.List;

public interface CategoryProvider {
    List<Category> getCategories();
}