package ru.bsuedu.cad.lab.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.bsuedu.cad.lab.dto.CreateOrderDTO;
import ru.bsuedu.cad.lab.dto.CustomerDTO;
import ru.bsuedu.cad.lab.dto.OrderDTO;
import ru.bsuedu.cad.lab.mapper.CustomerMapper;
import ru.bsuedu.cad.lab.mapper.OrderMapper;
import ru.bsuedu.cad.lab.service.CustomerService;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.OrderService.Item;

@Controller
@RequestMapping("/view/v1/order")
public class OrderViewController {
    
    final private OrderService orderService;
    final private CustomerService customerService;
    

    public OrderViewController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("")
    public String getOrders(Model model){
        var orders = orderService.getOrders();
        model.addAttribute("orders" , orders.stream().map(OrderMapper::toDTO).collect(Collectors.toList()));
        return "order-list";
    }

    @GetMapping("/create")
    public String addOrders(Model model){
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        List<CustomerDTO> customerDTOs = customerService.getCustomers().stream().map(CustomerMapper::toDTO).collect(Collectors.toList());
        model.addAttribute("order", createOrderDTO);
        model.addAttribute("customers", customerDTOs);
        return "order-create";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute CreateOrderDTO createOrderDTO){
        orderService.createOrder(createOrderDTO.getCustomerId(),createOrderDTO.getAddress(), new ArrayList<Item>());
        return "redirect:/view/v1/order";
    }

    @GetMapping("/edit/{id}")
    public String editOrders(@PathVariable(name = "id") int id, Model model){
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        var edit = orderService.getById(id);
        createOrderDTO.setAddress(edit.getShippingAddress());
        createOrderDTO.setCustomerId(edit.getCustomer().getCustomerId());
        createOrderDTO.setOrderId(id);
        List<CustomerDTO> customerDTOs = customerService.getCustomers().stream().map(CustomerMapper::toDTO).collect(Collectors.toList());
        model.addAttribute("order", createOrderDTO);
        model.addAttribute("customers", customerDTOs);
        return "order-edit";
    }

    @PostMapping("/edit")
    public String updateOrder(@ModelAttribute CreateOrderDTO createOrderDTO){
        orderService.updateOrder(createOrderDTO.getOrderId(), null, null, createOrderDTO.getCustomerId(), createOrderDTO.getAddress());
        return "redirect:/view/v1/order";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id") int id){
        orderService.deleteById(id);
        return "redirect:/view/v1/order";
    }
}
