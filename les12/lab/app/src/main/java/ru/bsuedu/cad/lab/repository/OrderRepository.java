package ru.bsuedu.cad.lab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
  @Query("SELECT DISTINCT o FROM Order o " +
      "LEFT JOIN FETCH o.orderDetails d " +
      "LEFT JOIN FETCH d.product " +
      "LEFT JOIN FETCH o.customer")
  List<Order> findAllWithDetails();
}
