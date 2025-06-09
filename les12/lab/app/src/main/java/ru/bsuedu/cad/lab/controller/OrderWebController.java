package ru.bsuedu.cad.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.bsuedu.cad.lab.dto.CreateOrderDetailDto;
import ru.bsuedu.cad.lab.dto.CreateOrderDto;
import ru.bsuedu.cad.lab.dto.UpdateOrderDto;
import ru.bsuedu.cad.lab.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

	private final OrderService orderService;

	@Autowired
	public OrderWebController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public String getAllOrders(Model model) {
		model.addAttribute("orders", orderService.getAllOrders());
		return "orders/list";
	}

	@GetMapping("/{id}")
	public String getOrderById(@PathVariable("id") int id, Model model) {
		model.addAttribute("order", orderService.getAllOrders().stream()
				.filter(o -> o.getOrderId() == id)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Order not found")));
		return "orders/details";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("order", new CreateOrderDto());
		return "orders/create";
	}

	@PostMapping
	public String createOrder(@ModelAttribute CreateOrderDto order) {
		CreateOrderDetailDto detail = order.getDetails().get(0);
		orderService.createOrder(order.getCustomerId(), detail.getProductId(), detail.getQuantity());
		return "redirect:/orders";
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable("id") int id, Model model) {
		model.addAttribute("order", orderService.getAllOrders().stream()
				.filter(o -> o.getOrderId() == id)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Order not found")));
		return "orders/edit";
	}

	@PostMapping("/{id}")
	public String updateOrder(@PathVariable("id") int id, @ModelAttribute UpdateOrderDto order) {
		orderService.updateOrder(id, order);
		return "redirect:/orders";
	}

	@PostMapping("/{id}/delete")
	public String deleteOrder(@PathVariable("id") int id) {
		orderService.deleteOrder(id);
		return "redirect:/orders";
	}
}