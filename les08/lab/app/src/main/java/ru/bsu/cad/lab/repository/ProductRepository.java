package ru.bsu.cad.lab.repository;

import ru.bsu.cad.lab.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductRepository {

    private final EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Product entity) {
        entityManager.persist(entity);
    }

    public Product findById(int id) {
        return entityManager.find(Product.class, id);
    }

    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT e FROM Product e", Product.class);
        return query.getResultList();
    }
}