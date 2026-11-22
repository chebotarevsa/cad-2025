package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.OrderEntity;

import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(OrderEntity order) {
        entityManager.persist(order);
    }

    public OrderEntity findById(Long id) {
        return entityManager.find(OrderEntity.class, id);
    }

    public List<OrderEntity> findAll() {
        return entityManager
                .createQuery("FROM OrderEntity", OrderEntity.class)
                .getResultList();
    }
}