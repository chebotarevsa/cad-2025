package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.lab.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
