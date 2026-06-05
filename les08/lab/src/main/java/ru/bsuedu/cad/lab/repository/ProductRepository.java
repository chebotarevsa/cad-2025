package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Product;

import java.util.List;

@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Product product) {
        entityManager.persist(product);
    }

    public Product findById(Long id) {
        return entityManager.find(Product.class, id);
    }

    public List<Product> findAll() {
        return entityManager
                .createQuery("FROM Product", Product.class)
                .getResultList();
    }
}