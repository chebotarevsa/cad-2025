package ru.bsuedu.cad.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.model.OrderForm;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderItem;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

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
        model.addAttribute("orders", orderService.findAllOrders());
        return "orders";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        addDictionaries(model);
        return "order-form";
    }

    @PostMapping
    public String create(@ModelAttribute OrderForm form) {
        orderService.createOrder(
                form.getCustomerId(),
                List.of(new OrderItem(form.getProductId(), form.getQuantity())),
                form.getShippingAddress()
        );
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        Order order = orderService.findOrder(id);
        OrderForm form = new OrderForm();
        form.setCustomerId(order.getCustomer().getId());
        form.setShippingAddress(order.getShippingAddress());
        form.setStatus(order.getStatus());

        if (!order.getDetails().isEmpty()) {
            OrderDetail detail = order.getDetails().get(0);
            form.setProductId(detail.getProduct().getId());
            form.setQuantity(detail.getQuantity());
        }

        model.addAttribute("order", order);
        model.addAttribute("orderForm", form);
        addDictionaries(model);
        return "order-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute OrderForm form) {
        orderService.updateOrder(
                id,
                form.getCustomerId(),
                List.of(new OrderItem(form.getProductId(), form.getQuantity())),
                form.getShippingAddress(),
                form.getStatus()
        );
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    private void addDictionaries(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
    }
}
