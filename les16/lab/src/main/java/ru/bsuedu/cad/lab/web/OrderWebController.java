package ru.bsuedu.cad.lab.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

@Controller
@RequestMapping("/orders")
@PreAuthorize("hasAnyRole('USER','MANAGER')")
public class OrderWebController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderWebController(OrderService orderService,
                              OrderRepository orderRepository,
                              CustomerRepository customerRepository,
                              ProductRepository productRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listOrders(Model model, HttpServletRequest request) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("isManager", request.isUserInRole("MANAGER"));
        return "orders";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('MANAGER')")
    public String showCreateForm(Model model) {
        model.addAttribute("orderForm", new OrderFormData());
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("action", "/orders");
        model.addAttribute("pageTitle", "Create Order");
        return "order-form";
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public String createOrder(@ModelAttribute OrderFormData orderForm, RedirectAttributes redirectAttributes) {
        Map<Long, Integer> productQuantities = parseProductIds(orderForm.getProductIds());
        orderService.createOrder(orderForm.getCustomerId(), productQuantities, orderForm.getShippingAddress());
        redirectAttributes.addFlashAttribute("message", "Order created successfully");
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('MANAGER')")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Order not found");
            return "redirect:/orders";
        }
        OrderFormData orderForm = new OrderFormData();
        orderForm.setCustomerId(order.getCustomer() != null ? order.getCustomer().getCustomerId() : null);
        orderForm.setShippingAddress(order.getShippingAddress());
        orderForm.setStatus(order.getStatus());
        model.addAttribute("orderForm", orderForm);
        model.addAttribute("orderId", id);
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("action", "/orders/" + id);
        model.addAttribute("pageTitle", "Edit Order");
        return "order-form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public String updateOrder(@PathVariable Long id, @ModelAttribute OrderFormData orderForm, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrder(id, orderForm.getStatus(), orderForm.getShippingAddress());
            redirectAttributes.addFlashAttribute("message", "Order updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to update order");
        }
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('MANAGER')")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("message", "Order deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to delete order");
        }
        return "redirect:/orders";
    }

    private Map<Long, Integer> parseProductIds(String productIds) {
        Map<Long, Integer> result = new HashMap<>();
        if (productIds == null || productIds.isBlank()) {
            return result;
        }
        for (String item : productIds.split(",")) {
            String trimmed = item.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                Long id = Long.valueOf(trimmed);
                result.merge(id, 1, Integer::sum);
            } catch (NumberFormatException e) {
                // ignore invalid values
            }
        }
        return result;
    }
}
