package ru.bsuedu.cad.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.repository.CustomerOrderRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private CustomerOrderRepository orderRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ProductRepository productRepo;

    @GetMapping
    public String listOrders(Model model) {
        List<CustomerOrder> orders = orderRepo.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("customers", customerRepo.findAll());
        model.addAttribute("products", productRepo.findAll());
        return "order-form";
    }

    @PostMapping("/create")
    public String createOrder(
            @RequestParam int customerId,
            @RequestParam List<Integer> productIds
    ) {
        var customer = customerRepo.findById(customerId).orElseThrow();
        var total = productIds.stream()
                .map(id -> productRepo.findById(id).orElse(null))
                .filter(p -> p != null)
                .map(p -> p.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var order = new CustomerOrder();
        order.setCustomer(customer);
        order.setTotalPrice(total);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Создан");
        order.setShippingAddress(customer.getAddress());

        orderRepo.save(order);
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable int id) {
        orderRepo.deleteById(id);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        var order = orderRepo.findById(id).orElseThrow();
        model.addAttribute("order", order);
        return "edit-order";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(
            @PathVariable int id,
            @RequestParam String status,
            @RequestParam String shippingAddress
    ) {
        var order = orderRepo.findById(id).orElseThrow();
        order.setStatus(status);
        order.setShippingAddress(shippingAddress);
        orderRepo.save(order);
        return "redirect:/orders";
    }

}