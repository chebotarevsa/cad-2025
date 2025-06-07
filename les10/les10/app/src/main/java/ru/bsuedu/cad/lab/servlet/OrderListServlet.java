package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Order;

@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderListServlet.class);

    private ru.bsuedu.cad.lab.service.OrderService orderService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(ru.bsuedu.cad.lab.service.OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orderList = orderService.getOrders();

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><meta charset='UTF-8'><title>Список заказов</title></head><body>");
        out.println("<h1>Список заказов</h1>");
        out.println("<a href='" + req.getContextPath() + "/order/create'>Создать заказ</a>");
        out.println("<table border='1' cellpadding='5' cellspacing='0'>");
        out.println("<tr><th>ID</th><th>Дата</th><th>Статус</th><th>Адрес</th><th>Сумма</th></tr>");

        for (Order order : orderList) {
            out.println("<tr>");
            out.println("<td>" + order.getOrderID() + "</td>");
            out.println("<td>" + order.getOrderDate() + "</td>");
            out.println("<td>" + order.getStatus() + "</td>");
            out.println("<td>" + order.getShippingAddress() + "</td>");
            out.println("<td>" + order.getTotalPrice() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</body></html>");
    }
}
