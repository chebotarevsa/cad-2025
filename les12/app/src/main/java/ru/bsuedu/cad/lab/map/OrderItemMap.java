package ru.bsuedu.cad.lab.map;

import ru.bsuedu.cad.lab.dto.CreateOrderItemDto;
import ru.bsuedu.cad.lab.dto.OrderItemDto;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.service.OrderService;

public class OrderItemMap {
    public static OrderItemDto toDto(OrderDetail i)
    {
        var orderItemDto = new OrderItemDto();
            orderItemDto.setOrderDetailId(i.getOrderDetailId());
            orderItemDto.setQuantity(i.getQuantity());
            orderItemDto.setPrice(i.getPrice());
            orderItemDto.setProductDto(ProductMap.toDto(i.getProduct()));
            return orderItemDto;
    } 
    
    public static OrderService.Item toItem(CreateOrderItemDto i)
    {
        return new OrderService.Item(i.getProductId(), i.getQuantity());
    }
}
