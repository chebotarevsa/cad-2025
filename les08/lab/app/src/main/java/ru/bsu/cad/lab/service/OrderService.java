package ru.bsu.cad.lab.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ru.bsu.cad.lab.entity.Order;
import ru.bsu.cad.lab.repository.OrderRepository;

public class OrderService {

    private final EntityManager entityManager;
    private final OrderRepository orderRepository;

    public OrderService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.orderRepository = new OrderRepository(entityManager);
    }

    public void createOrder(Order order) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            orderRepository.save(order);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}