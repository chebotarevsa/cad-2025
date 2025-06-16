package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
