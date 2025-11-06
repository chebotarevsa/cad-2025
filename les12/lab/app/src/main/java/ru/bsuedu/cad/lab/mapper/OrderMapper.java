package ru.bsuedu.cad.lab.mapper;

import java.util.stream.Collectors;

import ru.bsuedu.cad.lab.dto.OrderDto;
import ru.bsuedu.cad.lab.dto.UpdateOrderDto;
import ru.bsuedu.cad.lab.entity.Order;

public class OrderMapper {
    public static OrderDto toDto(Order o){
        var orderDto = new OrderDto(); 
        orderDto.setOrderId(o.getOrderId());  
        orderDto.setOrderDate(o.getOrderDate());
        orderDto.setTotalPrice(o.getTotalPrice());
        orderDto.setStatus(o.getStatus());
        orderDto.setShippingAddress(o.getShippingAddress());
        var orderItemList = o.getItems();
        var orderItemDtoList = orderItemList.stream().map(OrderItemMapper::toDto).collect(Collectors.toList());
        orderDto.setItems(orderItemDtoList);
        orderDto.setCustomer(CustomerMapper.toDto(o.getCustomer()));
        return orderDto;
    }

    public static UpdateOrderDto toUpdateDto (Order o)
    {
        

        var updateOrderDto = new UpdateOrderDto();
        updateOrderDto.setId(o.getOrderId());
        updateOrderDto.setCustomerId(o.getCustomer().getCustomerId());
        updateOrderDto.setAddress(o.getShippingAddress());
        return updateOrderDto;
    }

}
