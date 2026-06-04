package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.ShopOrder;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.web.HtmlEscape;

public class OrderListServlet extends HttpServlet {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html lang=\"ru\"><head><meta charset=\"UTF-8\">");
        out.println("<title>Заказы</title></head><body>");
        out.println("<h1>Заказы</h1>");
        out.println("<p><a href=\"" + req.getContextPath() + "/orders/new\">Создать заказ</a></p>");
        out.println("<table border=\"1\" cellpadding=\"8\">");
        out.println("<tr><th>ID</th><th>Клиент</th><th>Дата</th><th>Сумма</th><th>Статус</th></tr>");

        for (ShopOrder order : orderService.findAllOrders()) {
            out.println("<tr>");
            out.println("<td>" + order.getOrderId() + "</td>");
            out.println("<td>" + HtmlEscape.escape(order.getCustomer().getName()) + "</td>");
            out.println("<td>" + DATE_FORMAT.format(order.getOrderDate()) + "</td>");
            out.println("<td>" + order.getTotalPrice().toPlainString() + "</td>");
            out.println("<td>" + HtmlEscape.escape(order.getStatus()) + "</td>");
            out.println("</tr>");
        }

        out.println("</table></body></html>");
        resp.flushBuffer();
    }
}
