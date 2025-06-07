package ru.bsu.cad.lab.repository;

import ru.bsu.cad.lab.entity.OrderDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderDetailRepository {

    private final EntityManager entityManager;

    public OrderDetailRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(OrderDetail entity) {
        entityManager.persist(entity);
    }

    public OrderDetail findById(int id) {
        return entityManager.find(OrderDetail.class, id);
    }

    public List<OrderDetail> findAll() {
        TypedQuery<OrderDetail> query = entityManager.createQuery("SELECT e FROM OrderDetail e", OrderDetail.class);
        return query.getResultList();
    }
}