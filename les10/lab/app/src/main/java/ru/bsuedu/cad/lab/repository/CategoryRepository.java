package ru.bsuedu.cad.lab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    default Category create(Category category) {
        return save(category);
    }

    @Override
    Optional<Category> findById(Integer id);

    @Override
    List<Category> findAll();
}
