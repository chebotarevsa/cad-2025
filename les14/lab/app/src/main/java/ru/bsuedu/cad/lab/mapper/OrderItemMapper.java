package ru.bsuedu.cad.lab.mapper;

import ru.bsuedu.cad.lab.dto.CreateOrderDTO;
import ru.bsuedu.cad.lab.dto.CreateOrderItemDTO;
import ru.bsuedu.cad.lab.dto.OrderItemDTO;
import ru.bsuedu.cad.lab.dto.ProductDTO;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.service.OrderService;

public class OrderItemMapper {
    public static OrderItemDTO toDTO(OrderDetail o){
        ProductDTO productDTO = ProductMapper.toDTO(o.getProduct());
        return new OrderItemDTO(o.getOrderDetailId(), o.getQuantity(), o.getPrice(), productDTO);
    }

    public static OrderService.Item toItem(CreateOrderItemDTO item){
        return new OrderService.Item(item.getProductId(), item.getQuantity());
    }  
}
