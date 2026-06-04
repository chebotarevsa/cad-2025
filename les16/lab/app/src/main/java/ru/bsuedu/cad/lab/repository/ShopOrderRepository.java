package ru.bsuedu.cad.lab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.ShopOrder;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Integer> {

    default ShopOrder create(ShopOrder order) {
        return save(order);
    }

    @Query(
            "SELECT DISTINCT o FROM ShopOrder o JOIN FETCH o.customer "
                    + "LEFT JOIN FETCH o.orderDetails d LEFT JOIN FETCH d.product ORDER BY o.orderId")
    List<ShopOrder> findAllWithCustomer();

    @Query(
            "SELECT o FROM ShopOrder o JOIN FETCH o.customer "
                    + "LEFT JOIN FETCH o.orderDetails d LEFT JOIN FETCH d.product WHERE o.orderId = :orderId")
    Optional<ShopOrder> findByIdWithDetails(int orderId);
}
