package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> { }