package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.lab.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
