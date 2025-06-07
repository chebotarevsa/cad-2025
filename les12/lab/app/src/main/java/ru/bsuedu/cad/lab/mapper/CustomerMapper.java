package ru.bsuedu.cad.lab.mapper;

import ru.bsuedu.cad.lab.dto.CustomerDto;
import ru.bsuedu.cad.lab.entity.Customer;

public class CustomerMapper {
    public static CustomerDto toDto(Customer customer)
    {
        return new CustomerDto(customer.getCustomerId(), customer.getName(), customer.getEmail(), customer.getPhone(), customer.getAddress());
    }
}
