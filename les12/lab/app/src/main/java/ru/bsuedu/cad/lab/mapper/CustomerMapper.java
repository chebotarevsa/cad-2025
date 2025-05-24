package ru.bsuedu.cad.lab.mapper;

import ru.bsuedu.cad.lab.dto.CustomerDTO;
import ru.bsuedu.cad.lab.entity.Customer;


public class CustomerMapper {
    public static CustomerDTO toDTO(Customer c){
        return new CustomerDTO(c.getCustomerId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress());
    }
}
