package ru.bsuedu.cad.lab.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/create")
    public String createOrderForm(Model model) {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        List<Product> products = (List<Product>) productRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("products", products);
        model.addAttribute("orderRequest", new OrderRestController.OrderRequest());
        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam int customerId, @RequestParam List<Integer> productIds) {
        orderService.createOrder(customerId, productIds);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable int id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            List<Customer> customers = (List<Customer>) customerRepository.findAll();
            List<Product> products = (List<Product>) productRepository.findAll();
            model.addAttribute("order", order);
            model.addAttribute("customers", customers);
            model.addAttribute("products", products);
            return "order/edit";
        }
        return "redirect:/orders";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable int id, @RequestParam int customerId, @RequestParam List<Integer> productIds) {
        orderService.updateOrder(id, customerId, productIds);
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}