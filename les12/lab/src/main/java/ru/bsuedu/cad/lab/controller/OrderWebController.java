package ru.bsuedu.cad.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.bsuedu.cad.lab.dto.OrderRequest;
import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.service.CustomerService;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.ProductService;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderWebController(OrderService orderService,
                              CustomerService customerService,
                              ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "orders";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("orderForm", new OrderRequest());
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("formAction", "/orders");
        model.addAttribute("pageTitle", "Новый заказ");
        return "order-form";
    }

    @PostMapping
    public String create(@ModelAttribute("orderForm") OrderRequest form, RedirectAttributes redirectAttributes) {
        try {
            orderService.createOrder(
                    form.getCustomerId(),
                    form.getProductId(),
                    form.getQuantity(),
                    form.getStatus(),
                    form.getShippingAddress());
            return "redirect:/orders";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        CustomerOrder order = orderService.findById(id);
        OrderRequest form = new OrderRequest();
        form.setCustomerId(order.getCustomer().getCustomerId());
        form.setStatus(order.getStatus());
        form.setShippingAddress(order.getShippingAddress());
        if (!order.getDetails().isEmpty()) {
            OrderDetail detail = order.getDetails().get(0);
            form.setProductId(detail.getProduct().getProductId());
            form.setQuantity(detail.getQuantity());
        }
        model.addAttribute("orderForm", form);
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("formAction", "/orders/" + id + "/edit");
        model.addAttribute("pageTitle", "Редактирование заказа #" + id);
        return "order-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id,
                         @ModelAttribute("orderForm") OrderRequest form,
                         RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrder(
                    id,
                    form.getCustomerId(),
                    form.getProductId(),
                    form.getQuantity(),
                    form.getStatus(),
                    form.getShippingAddress());
            return "redirect:/orders";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }
}
