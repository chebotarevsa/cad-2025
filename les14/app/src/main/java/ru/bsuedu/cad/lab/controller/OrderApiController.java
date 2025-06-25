package ru.bsuedu.cad.lab.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bsuedu.cad.lab.dto.CreateOrderDto;
import ru.bsuedu.cad.lab.dto.CreateOrderItemDto;
import ru.bsuedu.cad.lab.dto.OrderDto;
import ru.bsuedu.cad.lab.map.OrderItemMap;
import ru.bsuedu.cad.lab.map.OrderMap;
import ru.bsuedu.cad.lab.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    final private OrderService orderService;

    

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }
    

    @GetMapping("")
    public List<OrderDto> orders()
    {
        var orderList = orderService.getOrders();
        return orderList.stream().map(OrderMap::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto order(@PathVariable(name = "id") Long id)
    {
        var currentOrder = orderService.getOrderById(id);
        return OrderMap.toDto(currentOrder); 
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable(name = "id") Long id)
    {
        orderService.deleteOrder(id);
    }

    @PostMapping("")
    public OrderDto createOrder(@RequestBody CreateOrderDto orderDto)
    {
        var items = orderDto.getItems().stream().map(OrderItemMap::toItem).collect(Collectors.toList());
        var order = orderService.createOrder(orderDto.getCustomerId(), items, orderDto.getAddress());
        return OrderMap.toDto(order);
    }

    @PatchMapping("{id}/{itemId}")
    public void editItem(@PathVariable(name = "id") Long id, @PathVariable(name = "itemId") Long itemId, @RequestBody CreateOrderItemDto orderItemDto)
    {
        orderService.editItem(id, itemId, new OrderService.Item(orderItemDto.getProductId(), orderItemDto.getQuantity()));
    }
    
}

