package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import ru.bsuedu.cad.lab.entity.OrderEntity;

import java.util.List;

public class OrderRepository {

    private EntityManagerFactory emf;

    public OrderRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(OrderEntity order) {
        System.out.println("=== Saving order ===");
        System.out.println("Order customer: " + order.getCustomer());
        System.out.println("Order createdAt: " + order.getCreatedAt());

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
            System.out.println("Order saved successfully with ID: " + order.getId());
        } catch (Exception e) {
            System.out.println("Error saving order: " + e.getMessage());
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public OrderEntity findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrderEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<OrderEntity> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("FROM OrderEntity", OrderEntity.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}