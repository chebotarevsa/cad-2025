package ru.bsuedu.cad.lab.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    
}
