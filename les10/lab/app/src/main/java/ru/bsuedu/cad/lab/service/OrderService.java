package ru.bsuedu.cad.lab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    @Transactional
    public void createOrder(Order order) {
        log.info("Creating order in transaction: {}", order);
        repository.save(order);
        log.info("Order created successfully: {}", order);
    }

    public List<Order> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

}
