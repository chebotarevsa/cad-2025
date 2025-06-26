package ru.bsuedu.cad.lab.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    
}
