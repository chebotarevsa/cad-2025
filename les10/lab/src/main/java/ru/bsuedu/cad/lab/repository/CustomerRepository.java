package ru.bsuedu.cad.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import ru.bsuedu.cad.lab.entity.Customer;

import java.util.List;

public class CustomerRepository {

    private EntityManagerFactory emf;

    public CustomerRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(Customer customer) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Customer findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public List<Customer> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("FROM Customer", Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}