package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
