package ru.bsuedu.cad.lab.mapper;

import java.util.stream.Collectors;

import ru.bsuedu.cad.lab.dto.CustomerDTO;
import ru.bsuedu.cad.lab.dto.OrderDTO;
import ru.bsuedu.cad.lab.entity.Order;

public class OrderMapper {
    public static OrderDTO toDTO(Order o){
        CustomerDTO customerDTO = CustomerMapper.toDTO(o.getCustomer());
        var items =  o.getOrderDetails().stream().map(OrderItemMapper::toDTO).collect(Collectors.toList());
        return new OrderDTO(o.getOrderId(), o.getOrderDate(), o.getTotalPrice(), o.getStatus(), o.getShippingAddress(), customerDTO, items);
    } 
}
