package ru.bsuedu.cad.lab.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.repository.OrderDetailRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderDetailRepositoryImpl implements OrderDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        if (orderDetail.getId() == null) {
            entityManager.persist(orderDetail);
            return orderDetail;
        } else {
            return entityManager.merge(orderDetail);
        }
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return Optional.ofNullable(entityManager.find(OrderDetail.class, id));
    }

    @Override
    public List<OrderDetail> findAll() {
        return entityManager.createQuery("SELECT od FROM OrderDetail od", OrderDetail.class)
                .getResultList();
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return entityManager.createQuery(
                        "SELECT od FROM OrderDetail od WHERE od.order.id = :orderId", OrderDetail.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}