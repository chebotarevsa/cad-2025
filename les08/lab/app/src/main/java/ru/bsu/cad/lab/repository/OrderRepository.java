package ru.bsu.cad.lab.repository;

import ru.bsu.cad.lab.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderRepository {

    private final EntityManager entityManager;

    public OrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Order entity) {
        entityManager.persist(entity);
    }

    public Order findById(int id) {
        return entityManager.find(Order.class, id);
    }

    public List<Order> findAll() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT e FROM Order e", Order.class);
        return query.getResultList();
    }
}