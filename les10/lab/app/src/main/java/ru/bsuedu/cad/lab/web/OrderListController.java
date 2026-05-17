package ru.bsuedu.cad.lab.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders")
public class OrderListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        OrderService orderService = ctx.getBean(OrderService.class);
        List<Order> orders = orderService.getAllOrders();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Список заказов</title></head><body>");
        out.println("<h1>Заказы</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Покупатель</th><th>Дата</th><th>Сумма</th><th>Статус</th><th>Адрес</th></tr>");
        for (Order o : orders) {
            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>%n",
                    o.getOrderId(), o.getCustomer().getName(), o.getOrderDate(),
                    o.getTotalPrice().toString(), o.getStatus(), o.getShippingAddress());
        }
        out.println("</table>");
        out.println("<br><a href='/petstore/createOrder'>Создать заказ</a>");
        out.println("</body></html>");
    }
}