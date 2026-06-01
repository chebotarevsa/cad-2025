package ru.bsuedu.cad.lab.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
            entityManager.persist(order);
            return order;
        } else {
            return entityManager.merge(order);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        Order order = entityManager.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "LEFT JOIN FETCH o.orderDetails od " +
                                "LEFT JOIN FETCH od.product " +
                                "WHERE o.id = :id", Order.class)
                .setParameter("id", id)
                .getSingleResult();
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = entityManager.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "LEFT JOIN FETCH o.orderDetails od " +
                                "LEFT JOIN FETCH od.product", Order.class)
                .getResultList();
        return orders;
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return entityManager.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "LEFT JOIN FETCH o.orderDetails od " +
                                "LEFT JOIN FETCH od.product " +
                                "WHERE o.customer.id = :customerId", Order.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }
}