package ru.bsuedu.cad.lab.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        try {
            Order order = entityManager.createQuery(
                            "SELECT DISTINCT o FROM Order o " +
                                    "LEFT JOIN FETCH o.customer " +
                                    "LEFT JOIN FETCH o.orderDetails od " +
                                    "LEFT JOIN FETCH od.product p " +
                                    "LEFT JOIN FETCH p.category " +
                                    "WHERE o.id = :id", Order.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return entityManager.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "LEFT JOIN FETCH o.customer " +
                                "LEFT JOIN FETCH o.orderDetails od " +
                                "LEFT JOIN FETCH od.product p " +
                                "LEFT JOIN FETCH p.category", Order.class)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(Long customerId) {
        return entityManager.createQuery(
                        "SELECT o FROM Order o WHERE o.customer.id = :id", Order.class)
                .setParameter("id", customerId)
                .getResultList();
    }

    @Override
    public void delete(Order order) {
        entityManager.remove(entityManager.contains(order) ? order : entityManager.merge(order));
    }
}