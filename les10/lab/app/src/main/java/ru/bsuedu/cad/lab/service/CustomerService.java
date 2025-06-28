package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}