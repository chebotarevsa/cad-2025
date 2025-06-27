package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"customer", "orderDetails", "orderDetails.product"})
    @Query("SELECT DISTINCT o FROM Order o")
    List<Order> findAllWithDetails();

    @EntityGraph(attributePaths = {"customer", "orderDetails", "orderDetails.product"})
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(Long id);

    @Override
    @EntityGraph(attributePaths = {})
    List<Order> findAll();

    @Override
    @EntityGraph(attributePaths = {})
    Optional<Order> findById(Long id);
}