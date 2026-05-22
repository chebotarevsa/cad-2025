package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrdersServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");

        OrderService orderService = WebUtils.getBean(getServletContext(), OrderService.class);
        List<Order> orders = orderService.findAllOrders();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!doctype html>");
            out.println("<html lang='ru'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Заказы</title>");
            out.println("<style>");
            out.println("body{font-family:Arial,sans-serif;margin:20px;}");
            out.println("table{border-collapse:collapse;}");
            out.println("th,td{border:1px solid black;padding:6px;}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Заказы магазина зоотоваров</h1>");
            out.println("<p><a href='" + request.getContextPath() + "/orders/new'>Создать заказ</a></p>");

            if (orders.isEmpty()) {
                out.println("<p>Заказов пока нет.</p>");
            } else {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Клиент</th><th>Дата</th><th>Статус</th><th>Адрес</th><th>Товары</th><th>Итого</th></tr>");
                for (Order order : orders) {
                    out.println("<tr>");
                    out.println("<td>" + order.getId() + "</td>");
                    out.println("<td>" + WebUtils.html(order.getCustomer().getName()) + "</td>");
                    out.println("<td>" + DATE_FORMAT.format(order.getOrderDate()) + "</td>");
                    out.println("<td>" + WebUtils.html(order.getStatus()) + "</td>");
                    out.println("<td>" + WebUtils.html(order.getShippingAddress()) + "</td>");
                    out.println("<td>");
                    for (OrderDetail detail : order.getDetails()) {
                        out.println(WebUtils.html(detail.getProduct().getName())
                                + " x " + detail.getQuantity() + "<br>");
                    }
                    out.println("</td>");
                    out.println("<td>" + order.getTotalPrice() + " руб.</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }
}
