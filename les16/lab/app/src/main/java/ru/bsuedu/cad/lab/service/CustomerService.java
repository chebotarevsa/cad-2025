package ru.bsuedu.cad.lab.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.repository.CustomerRepository;

@Service
public class CustomerService {
    
    final private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers(){
        List<Customer> customers = new ArrayList<>();
        for (Customer customer : customerRepository.findAll()) {
            customers.add(customer);
        }
        return customers;
    }
    
}
