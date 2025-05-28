package ru.bsuedu.cad.lab.provider;

import ru.bsuedu.cad.lab.model.Category;

import java.util.List;

public interface CategoryProvider {
    List<Category> getCategories();
}
