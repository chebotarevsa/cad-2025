package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bsuedu.cad.lab.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}