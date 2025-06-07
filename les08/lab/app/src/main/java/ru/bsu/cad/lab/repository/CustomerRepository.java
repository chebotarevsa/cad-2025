package ru.bsu.cad.lab.repository;

import ru.bsu.cad.lab.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CustomerRepository {

    private final EntityManager entityManager;

    public CustomerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Customer entity) {
        entityManager.persist(entity);
    }

    public Customer findById(int id) {
        return entityManager.find(Customer.class, id);
    }

    public List<Customer> findAll() {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT e FROM Customer e", Customer.class);
        return query.getResultList();
    }
}