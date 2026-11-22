package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import ru.bsuedu.cad.lab.entity.Category;

import java.util.List;

public class CategoryRepository {

    private EntityManagerFactory emf;

    public CategoryRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(Category category) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Category findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public List<Category> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("FROM Category", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}