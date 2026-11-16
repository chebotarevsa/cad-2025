package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
}