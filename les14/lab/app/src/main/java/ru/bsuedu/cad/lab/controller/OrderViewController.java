package ru.bsuedu.cad.lab.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.service.CustomerService;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.ProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderViewController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderViewController(OrderService orderService,
                               CustomerService customerService,
                               ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable("id") Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        return orderService.getOrderById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "orders/view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Заказ не обнаружен");
                    return "redirect:/orders";
                });
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('MANAGER')")
    public String showCreateForm(Model model) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");

        model.addAttribute("order", order);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        return "orders/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public String createOrder(
            @RequestParam("productIds") List<Long> productIds,
            @RequestParam("quantities") List<Integer> quantities,
            @ModelAttribute("order") Order order,
            RedirectAttributes redirectAttributes) {

        try {
            Customer customer = customerService.getCustomerById(order.getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Несуществующий ID покупателя"));

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (int i = 0; i < productIds.size(); i++) {
                Product product = productService.getProductById(productIds.get(i))
                        .orElseThrow(() -> new IllegalArgumentException("Несуществующий ID товара"));

                OrderDetail detail = new OrderDetail();
                detail.setProduct(product);
                detail.setQuantity(quantities.get(i));
                detail.setPrice(product.getPrice());
                orderDetails.add(detail);
            }

            order.setCustomer(customer);
            order.setOrderDetails(orderDetails);

            orderService.createOrder(order);
            redirectAttributes.addFlashAttribute("success", "Заказ успешно создан!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/new";
        }

        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('MANAGER')")
    public String showEditForm(@PathVariable("id") Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        return orderService.getOrderById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("customers", customerService.getAllCustomers());
                    model.addAttribute("products", productService.getAllProducts());
                    return "orders/edit";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Заказ не найден");
                    return "redirect:/orders";
                });
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public String updateOrder(@PathVariable("id") Long id,
                              @ModelAttribute("order") Order order,
                              RedirectAttributes redirectAttributes) {
        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(order.getStatus());
                    existingOrder.setShippingAddress(order.getShippingAddress());
                    orderService.updateOrder(existingOrder);
                    redirectAttributes.addFlashAttribute("success", "Заказ обновлен усапешно!");
                    return "redirect:/orders";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Заказ не найден");
                    return "redirect:/orders";
                });
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('MANAGER')")
    public String deleteOrder(@PathVariable("id") Long id,
                              RedirectAttributes redirectAttributes) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("success", "Заказ удален успешно!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Заказ не найден");
        }
        return "redirect:/orders";
    }
}