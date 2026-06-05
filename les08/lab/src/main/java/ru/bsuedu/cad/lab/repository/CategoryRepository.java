package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Category;

import java.util.List;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Category category) {
        entityManager.persist(category);
    }

    public Category findById(Long id) {
        return entityManager.find(Category.class, id);
    }

    public List<Category> findAll() {
        return entityManager
                .createQuery("FROM Category", Category.class)
                .getResultList();
    }
}