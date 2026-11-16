package ru.bsuedu.cad.lab.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.dto.CreateOrderRequest;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

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
    @Transactional(readOnly = true)
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order-list";
    }

    @GetMapping("/new")
    @Transactional(readOnly = true)
    public String showCreateForm(Model model, @RequestParam(required = false) String error) {
        logger.info("Отображение формы создания заказа");

        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();

        model.addAttribute("customers", customers);
        model.addAttribute("products", products);

        CreateOrderRequest orderRequest = new CreateOrderRequest();
        List<CreateOrderRequest.OrderItemRequest> items = new ArrayList<>();
        items.add(new CreateOrderRequest.OrderItemRequest());
        orderRequest.setItems(items);

        model.addAttribute("orderRequest", orderRequest);

        if (error != null) {
            model.addAttribute("errorMessage", "Добавьте хотя бы один товар в заказ");
        }

        return "order-form";
    }

    @PostMapping
    @Transactional
    public String createOrder(@ModelAttribute CreateOrderRequest orderRequest) {
        logger.info("Создание заказа: customerId={}, address={}",
                orderRequest.getCustomerId(), orderRequest.getShippingAddress());

        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            logger.error("Список товаров пуст!");
            return "redirect:/orders/new?error=empty";
        }

        List<OrderService.OrderItemRequest> items = orderRequest.getItems().stream()
                .map(item -> new OrderService.OrderItemRequest(item.getProductId(), item.getQuantity()))
                .toList();

        orderService.createOrder(
                orderRequest.getCustomerId(),
                items,
                orderRequest.getShippingAddress()
        );

        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order-detail";
    }
}