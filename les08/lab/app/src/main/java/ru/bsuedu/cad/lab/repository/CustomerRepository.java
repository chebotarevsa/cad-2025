package ru.bsuedu.cad.lab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    default Customer create(Customer customer) {
        return save(customer);
    }

    @Override
    Optional<Customer> findById(Integer id);

    @Override
    List<Customer> findAll();
}
