package ru.bsuedu.cad.lab.mapper;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.dto.CustomerDto;
import ru.bsuedu.cad.lab.entity.Customer;

@Component
public class CustomerMapper {
  public CustomerDto toDto(Customer customer) {
    if (customer == null) {
      return null;
    }
    return new CustomerDto(
        customer.getCustomerId(),
        customer.getName(),
        customer.getEmail(),
        customer.getPhone(),
        customer.getAddress());
  }
}
