package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.lab.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
