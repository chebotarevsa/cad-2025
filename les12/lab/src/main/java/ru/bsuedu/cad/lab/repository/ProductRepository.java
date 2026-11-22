package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import ru.bsuedu.cad.lab.entity.Product;

import java.util.List;

public class ProductRepository {

    private EntityManagerFactory emf;

    public ProductRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(Product product) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Product findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public List<Product> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("FROM Product", Product.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}