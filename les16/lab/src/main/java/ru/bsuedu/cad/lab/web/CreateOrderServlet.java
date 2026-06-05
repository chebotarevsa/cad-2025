package ru.bsuedu.cad.lab.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

@WebServlet(urlPatterns = "/create-order")
public class CreateOrderServlet extends HttpServlet {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.customerRepository = ctx.getBean(CustomerRepository.class);
        this.productRepository = ctx.getBean(ProductRepository.class);
        this.orderService = ctx.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        var customers = customerRepository.findAll();
        var products = productRepository.findAll();

        try (PrintWriter w = resp.getWriter()) {
            w.println("<html><head><meta charset=\"utf-8\"><title>Create Order</title></head><body>");
            w.println("<h1>Create Order</h1>");
            w.println("<p>Введите id покупателя и id товаров через запятую.</p>");
            w.println("<form method=\"post\">");
            w.println("Customer id: <input type=\"text\" name=\"customerId\"/><br/><br/>");
            w.println("Product ids (comma-separated): <input type=\"text\" name=\"productIds\"/><br/><br/>");
            w.println("<input type=\"submit\" value=\"Create\"/> ");
            w.println("</form>");

            w.println("<h2>Available Customers</h2>");
            w.println("<ul>");
            for (var c : customers) {
                w.printf("<li>id=%d — %s</li>", c.getCustomerId(), c.getName());
            }
            w.println("</ul>");

            w.println("<h2>Available Products</h2>");
            w.println("<ul>");
            for (Product p : products) {
                w.printf("<li>id=%d — %s (stock: %s)</li>", p.getProductId(), p.getName(), p.getStockQuantity());
            }
            w.println("</ul>");

            w.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String customerParam = req.getParameter("customerId");
        String productIdsParam = req.getParameter("productIds");

        if (customerParam == null || customerParam.isBlank() || productIdsParam == null || productIdsParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/create-order");
            return;
        }

        Long customerId;
        try {
            customerId = Long.valueOf(customerParam.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/create-order");
            return;
        }

        Map<Long, Integer> productQuantities = new HashMap<>();
        for (String item : productIdsParam.split(",")) {
            String trimmed = item.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                Long productId = Long.valueOf(trimmed);
                productQuantities.merge(productId, 1, Integer::sum);
            } catch (NumberFormatException ignored) {
            }
        }

        if (!productQuantities.isEmpty()) {
            orderService.createOrder(customerId, productQuantities, "");
        }

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
