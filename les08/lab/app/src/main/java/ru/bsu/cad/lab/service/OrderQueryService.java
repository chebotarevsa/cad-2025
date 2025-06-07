package ru.bsu.cad.lab.service;

import jakarta.persistence.EntityManager;
import ru.bsu.cad.lab.entity.Order;
import ru.bsu.cad.lab.repository.OrderRepository;

import java.util.List;

public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryService(EntityManager entityManager) {
        this.orderRepository = new OrderRepository(entityManager);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}