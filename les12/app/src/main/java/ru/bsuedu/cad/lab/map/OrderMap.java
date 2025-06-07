package ru.bsuedu.cad.lab.map;

import java.util.stream.Collectors;

import ru.bsuedu.cad.lab.dto.OrderDto;
import ru.bsuedu.cad.lab.dto.UpdateOrderDto;
import ru.bsuedu.cad.lab.entity.Order;

public class OrderMap {
    public static OrderDto toDto(Order o)
    {
        var orderDto = new OrderDto(); 
        orderDto.setOrderID(o.getOrderID()); 
        orderDto.setOrderDate(o.getOrderDate());
        orderDto.setTotalPrice(o.getTotalPrice()); 
        orderDto.setStatus(o.getStatus()); 
        orderDto.setShippingAddress(o.getShippingAddress());
        var orderItemList = o.getItems();
        var orderItemDtoList = orderItemList.stream().map(OrderItemMap::toDto).collect(Collectors.toList());
        orderDto.setItems(orderItemDtoList);
        orderDto.setCustomerDto(CustomerMap.toDto(o.getCustomer()));
        return orderDto;
    }

    public static UpdateOrderDto toUpdateDto(Order o)
    {
        var updateOrderDto = new UpdateOrderDto();
        updateOrderDto.setId(o.getOrderID());
        updateOrderDto.setCustomerId(o.getCustomer().getCustomerID());
        updateOrderDto.setAddress(o.getShippingAddress());
        return updateOrderDto;
    }
}
