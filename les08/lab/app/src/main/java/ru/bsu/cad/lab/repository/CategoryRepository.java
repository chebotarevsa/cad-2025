package ru.bsu.cad.lab.repository;

import ru.bsu.cad.lab.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CategoryRepository {

    private final EntityManager entityManager;

    public CategoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Category entity) {
        entityManager.persist(entity);
    }

    public Category findById(int id) {
        return entityManager.find(Category.class, id);
    }

    public List<Category> findAll() {
        TypedQuery<Category> query = entityManager.createQuery("SELECT e FROM Category e", Category.class);
        return query.getResultList();
    }
}