package ru.bsuedu.cad.lab.controller;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.bsuedu.cad.lab.dto.CreateOrderDto;
import ru.bsuedu.cad.lab.dto.UpdateOrderDto;
import ru.bsuedu.cad.lab.map.CustomerMap;
import ru.bsuedu.cad.lab.map.OrderMap;
import ru.bsuedu.cad.lab.service.CustomerService;
import ru.bsuedu.cad.lab.service.OrderService;

@Controller
@RequestMapping("/view/order")
public class OrderViewController {

    final private OrderService orderService;
    final private CustomerService customerService;

    public OrderViewController(OrderService orderService,  CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("")
    public String orders(Model model)
    {
        var orderList = orderService.getOrders();
        var orders = orderList.stream().map(OrderMap::toDto).collect(Collectors.toList());
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateOrderForm(Model model)
    {
        model.addAttribute("order", new CreateOrderDto());
        model.addAttribute("customers", customerService.getCustomer().stream().map(CustomerMap::toDto).collect(Collectors.toList()));
        return "newOrder";
    }

    @PostMapping("/new")
    public String submitCreateOrderForm(@ModelAttribute CreateOrderDto orderDto)
    {
        var items = new ArrayList<OrderService.Item>();
        var order = orderService.createOrder(orderDto.getCustomerId(), items, orderDto.getAddress());
        return "redirect:/view/order";
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable(name = "id") Long id, Model model)
    {
        var currentOrder = orderService.getOrderById(id);
        var editedOrder = OrderMap.toUpdateDto(currentOrder); 
        model.addAttribute("order", editedOrder);
        model.addAttribute("customers", customerService.getCustomer().stream().map(CustomerMap::toDto).collect(Collectors.toList()));
        return "editOrder";
    }

    @PostMapping("/edit")
    public String submitCreateOrderForm(@ModelAttribute UpdateOrderDto updateOrderDto)
    {
        orderService.updateOrder(updateOrderDto.getId() ,updateOrderDto.getCustomerId(), updateOrderDto.getAddress());
        return "redirect:/view/order";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrderForm(@PathVariable(name = "id") Long id, Model model)
    {
        orderService.deleteOrder(id);
        return "redirect:/view/order";
    }
}
