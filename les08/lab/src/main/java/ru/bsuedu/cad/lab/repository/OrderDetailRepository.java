package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
