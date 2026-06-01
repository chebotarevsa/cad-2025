package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByCustomerId(Long customerId);
}