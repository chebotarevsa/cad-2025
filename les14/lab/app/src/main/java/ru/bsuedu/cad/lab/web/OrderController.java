package ru.bsuedu.cad.lab.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "orders";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "create-order";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long customerId,
                         @RequestParam Long productId,
                         @RequestParam Integer quantity,
                         @RequestParam String shippingAddress) {

        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        OrderDetail detail = new OrderDetail();
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setPrice(product.getPrice());

        Order order = new Order();
        order.setOrderId(1);
        order.setCustomer(customer);
        order.setShippingAddress(shippingAddress);
        order.setStatus("NEW");
        order.setOrderDate(java.time.LocalDateTime.now());

        order.setOrderDetails(java.util.List.of(detail));

        orderRepository.save(order);

        return "redirect:/orders";
    }
}
