package lab1.controller;

import lab1.entity.Order;
import lab1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/orders")
public class OrderWebController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/web/orders";
    }

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        return "create-order";
    }

    @PostMapping
    public String createOrder(
            @RequestParam Long customerId,
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam String shippingAddress) {

        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(productId);
        item.setQuantity(quantity);

        orderService.createOrder(customerId, List.of(item), shippingAddress);
        return "redirect:/web/orders";
    }
}