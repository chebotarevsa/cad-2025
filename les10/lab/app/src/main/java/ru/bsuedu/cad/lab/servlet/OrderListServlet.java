package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class OrderListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        OrderService orderService = ctx.getBean(OrderService.class);
        List<Order> orders = orderService.getAllOrders();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>Заказы</title>");
        out.println("<style>body{font-family:sans-serif;padding:20px}");
        out.println("table{border-collapse:collapse;width:100%}");
        out.println("th,td{border:1px solid #ccc;padding:8px;text-align:left}");
        out.println("th{background:#4a90d9;color:#fff}");
        out.println(".btn{padding:8px 16px;background:#4a90d9;color:#fff;border:none;cursor:pointer;text-decoration:none;border-radius:4px}</style></head><body>");
        out.println("<h1>Список заказов</h1>");
        out.println("<a class='btn' href='create-order'>Создать заказ</a><br><br>");

        if (orders.isEmpty()) {
            out.println("<p>Заказов пока нет.</p>");
        } else {
            out.println("<table><tr><th>#</th><th>Клиент</th><th>Дата</th><th>Сумма</th><th>Статус</th><th>Адрес доставки</th></tr>");
            for (Order o : orders) {
                out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%.2f руб.</td><td>%s</td><td>%s</td></tr>%n",
                        o.getOrderId(),
                        o.getCustomer().getName(),
                        o.getOrderDate().toLocalDate(),
                        o.getTotalPrice(),
                        o.getStatus(),
                        o.getShippingAddress());
            }
            out.println("</table>");
        }
        out.println("</body></html>");
    }
}
