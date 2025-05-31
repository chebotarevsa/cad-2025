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

import ru.bsuedu.cad.lab.dto.CreateOrderDTO;
import ru.bsuedu.cad.lab.dto.OrderDTO;
import ru.bsuedu.cad.lab.dto.UpdateOrderDTO;
import ru.bsuedu.cad.lab.mapper.OrderItemMapper;
import ru.bsuedu.cad.lab.mapper.OrderMapper;
import ru.bsuedu.cad.lab.service.OrderService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderAPIController {

    final private OrderService orderService;

    public OrderAPIController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public List<OrderDTO> getOrders(){
        var orders = orderService.getOrders();
        return orders.stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrdersById(@PathVariable(name = "id") int id){
        return OrderMapper.toDTO(orderService.getById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable(name = "id") int id){
        orderService.deleteById(id);
    }

    @PostMapping("")
    public OrderDTO createOrder(@RequestBody CreateOrderDTO newOrder){
        var items = newOrder.getItems().stream().map(OrderItemMapper::toItem).collect(Collectors.toList());
        var order = orderService.createOrder(newOrder.getCustomerId(),newOrder.getAddress(), items);
        return OrderMapper.toDTO(order);
    }

    @PatchMapping("/{id}")
    public void updateOrder(@PathVariable(name = "id") int id, @RequestBody UpdateOrderDTO orderDTO){
        orderService.updateOrder(id, orderDTO.getDate(), orderDTO.getStatus(), orderDTO.getCustomerId(), null);
    }

}
