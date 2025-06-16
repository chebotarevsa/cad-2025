package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  // Создание заказа
  @Transactional
  public Order createOrder(Order order) {
    return orderRepository.save(order);
  }

  // Получение всех заказов
  @Transactional
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }
}
