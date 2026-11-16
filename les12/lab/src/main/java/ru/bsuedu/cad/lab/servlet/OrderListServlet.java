package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils
            .getRequiredWebApplicationContext(getServletContext());
        orderService = ctx.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        List<Order> orders = orderService.getAllOrders();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Список заказов</title></head><body>");
        out.println("<h1>Список заказов</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Клиент</th><th>Дата</th><th>Статус</th><th>Сумма</th><table>");
        
        for (Order order : orders) {
            double total = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td></tr>",
                order.getId(),
                order.getCustomer().getFullName(),
                order.getOrderDate(),
                order.getStatus(),
                total);
        }
        
        out.println("</table>");
        out.println("<br><a href='create-order'>Создать новый заказ</a>");
        out.println("</body></html>");
    }
}