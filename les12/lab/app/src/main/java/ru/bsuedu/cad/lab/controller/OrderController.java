package ru.bsuedu.cad.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderService orderService,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "order-form";
    }

    @PostMapping
    public String create(@ModelAttribute OrderForm form) {
        Customer customer = customerRepository.findById(form.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));

        List<Product> products = form.getProductIds() == null ? List.of() :
                form.getProductIds().stream()
                        .map(id -> productRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + id)))
                        .collect(Collectors.toList());

        orderService.createOrder(customer, products, form.getShippingAddress());
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + id));

        OrderEditForm form = new OrderEditForm();
        form.setStatus(order.getStatus());
        form.setShippingAddress(order.getShippingAddress());

        model.addAttribute("order", order);
        model.addAttribute("editForm", form);
        model.addAttribute("statuses", List.of("NEW", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"));
        return "order-edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id, @ModelAttribute OrderEditForm form) {
        orderService.updateOrder(id, form.getStatus(), form.getShippingAddress());
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}
