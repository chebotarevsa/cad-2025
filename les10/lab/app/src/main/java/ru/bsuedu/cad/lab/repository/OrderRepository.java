package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("""
            select distinct o
            from Order o
            left join fetch o.customer
            left join fetch o.details d
            left join fetch d.product
            order by o.id
            """)
    List<Order> findAllWithDetails();
}
