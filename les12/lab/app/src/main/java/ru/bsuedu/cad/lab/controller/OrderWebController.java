package ru.bsuedu.cad.lab.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.ShopOrder;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderLineRequest;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.web.OrderForm;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderWebController(
            OrderService orderService,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "orders/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        addSelectLists(model);
        return "orders/create";
    }

    @PostMapping
    public String createOrder(@ModelAttribute OrderForm orderForm, RedirectAttributes redirectAttributes) {
        if (orderForm.getCustomerId() == null
                || orderForm.getProductId() == null
                || orderForm.getQuantity() == null
                || orderForm.getQuantity() < 1) {
            redirectAttributes.addFlashAttribute("error", "Заполните все поля формы");
            return "redirect:/orders/new";
        }
        orderService.createOrder(
                orderForm.getCustomerId(),
                orderForm.getShippingAddress(),
                List.of(new OrderLineRequest(orderForm.getProductId(), orderForm.getQuantity())));
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") int id, Model model) {
        ShopOrder order = orderService.findOrderById(id);
        OrderForm form = new OrderForm();
        form.setStatus(order.getStatus());
        form.setShippingAddress(order.getShippingAddress());
        model.addAttribute("orderId", id);
        model.addAttribute("orderForm", form);
        return "orders/edit";
    }

    @PostMapping("/{id}")
    public String updateOrder(@PathVariable("id") int id, @ModelAttribute OrderForm orderForm) {
        orderService.updateOrder(id, orderForm.getStatus(), orderForm.getShippingAddress());
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/orders";
    }

    private void addSelectLists(Model model) {
        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("products", products);
    }
}
