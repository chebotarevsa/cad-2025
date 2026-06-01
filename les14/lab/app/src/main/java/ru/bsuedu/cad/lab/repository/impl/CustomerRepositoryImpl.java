package ru.bsuedu.cad.lab.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            entityManager.persist(customer);
            return customer;
        } else {
            return entityManager.merge(customer);
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Customer.class, id));
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            Customer customer = entityManager.createQuery(
                            "SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(customer);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }
}