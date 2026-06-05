package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.OrderItem;

import java.util.List;

@Repository
public class OrderItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(OrderItem orderItem) {
        entityManager.persist(orderItem);
    }

    public OrderItem findById(Long id) {
        return entityManager.find(OrderItem.class, id);
    }

    public List<OrderItem> findAll() {
        return entityManager
                .createQuery("FROM OrderItem", OrderItem.class)
                .getResultList();
    }
}