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
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if (order.getId() == null) {
                em.persist(order);
            } else {
                em.merge(order);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
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

    public void delete(OrderEntity order) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            OrderEntity managed = em.contains(order) ? order : em.merge(order);
            em.remove(managed);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}