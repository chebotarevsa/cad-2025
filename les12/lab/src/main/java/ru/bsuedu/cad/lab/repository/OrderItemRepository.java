package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import ru.bsuedu.cad.lab.entity.OrderItem;

import java.util.List;

public class OrderItemRepository {

    private EntityManagerFactory emf;

    public OrderItemRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(OrderItem orderItem) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(orderItem);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public OrderItem findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrderItem.class, id);
        } finally {
            em.close();
        }
    }

    public List<OrderItem> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("FROM OrderItem", OrderItem.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}