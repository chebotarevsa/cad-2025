package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.OrderDetail;
import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository {
    OrderDetail save(OrderDetail orderDetail);
    Optional<OrderDetail> findById(Long id);
    List<OrderDetail> findAll();
    List<OrderDetail> findByOrderId(Long orderId);
}