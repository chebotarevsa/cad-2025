package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }
}