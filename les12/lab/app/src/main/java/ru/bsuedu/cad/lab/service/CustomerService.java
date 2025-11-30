package ru.bsuedu.cad.lab.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class CustomerService {
    final private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomer(){
        List<Customer> customerList = new ArrayList<>();
        for(Customer i : customerRepository.findAll())
        {
            customerList.add(i);
           
        }
        return customerList;

    }
}
