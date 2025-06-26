package ru.bsuedu.cad.lab.map;

import ru.bsuedu.cad.lab.dto.CustomerDto;
import ru.bsuedu.cad.lab.entity.Customer;

public class CustomerMap {
    public static CustomerDto toDto(Customer customer)
    {
        return new CustomerDto(customer.getCustomerID(), customer.getName(), customer.getEmail(), customer.getPhone(), customer.getAddress());
    }
}
