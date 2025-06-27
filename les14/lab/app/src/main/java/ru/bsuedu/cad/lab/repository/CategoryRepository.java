package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> { }