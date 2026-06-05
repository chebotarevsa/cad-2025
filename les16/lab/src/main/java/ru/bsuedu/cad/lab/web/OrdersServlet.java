package ru.bsuedu.cad.lab.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.repository.OrderRepository;

@WebServlet(urlPatterns = "/orders", loadOnStartup = 1)
public class OrdersServlet extends HttpServlet {

    private OrderRepository orderRepository;

    @Override
    public void init() throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.orderRepository = ctx.getBean(OrderRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        List<OrderEntity> orders = orderRepository.findAll();

        try (PrintWriter w = resp.getWriter()) {
            w.println("<html><head><meta charset=\"utf-8\"><title>Orders</title></head><body>");
            w.println("<h1>Orders</h1>");
            w.println("<a href=\"create-order\">Create order</a><br/><br/>");
            if (orders.isEmpty()) {
                w.println("<p>No orders</p>");
            } else {
                w.println("<table border=1 cellpadding=4><tr><th>Id</th><th>Customer</th><th>Date</th><th>Total</th><th>Status</th></tr>");
                for (OrderEntity o : orders) {
                    var customer = o.getCustomer() != null ? o.getCustomer().getName() : "-";
                    w.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", o.getOrderId(), customer, o.getOrderDate(), o.getTotalPrice(), o.getStatus());
                }
                w.println("</table>");
            }
            w.println("</body></html>");
        }
    }
}
