package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Customer;

import java.util.List;

@Repository
public class CustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Customer customer) {
        entityManager.persist(customer);
    }

    public Customer findById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    public List<Customer> findAll() {
        return entityManager
                .createQuery("FROM Customer", Customer.class)
                .getResultList();
    }
}