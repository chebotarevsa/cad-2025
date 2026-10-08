package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.service.OrderService;

public class OrderListServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        List<CustomerOrder> orders = orderService.findAllOrders();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String ctx = req.getContextPath();

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='ru'><head><meta charset='UTF-8'>");
        out.println("<title>Заказы — магазин зоотоваров</title>");
        out.println("<style>");
        out.println("body{font-family:Segoe UI,Arial,sans-serif;margin:24px;background:#f7f4ef;color:#222}");
        out.println("h1{margin-top:0}");
        out.println("table{border-collapse:collapse;width:100%;background:#fff}");
        out.println("th,td{border:1px solid #ddd;padding:8px;text-align:left}");
        out.println("th{background:#e8dff5}");
        out.println(".btn{display:inline-block;margin:12px 0;padding:10px 16px;background:#5b3a8c;color:#fff;");
        out.println("text-decoration:none;border-radius:6px}");
        out.println("</style></head><body>");
        out.println("<h1>Список заказов</h1>");
        out.println("<a class='btn' href='" + ctx + "/orders/new'>Создать заказ</a>");
        out.println("<p><a href='" + ctx + "/api/products'>REST: /api/products</a></p>");

        if (orders.isEmpty()) {
            out.println("<p>Заказов пока нет.</p>");
        } else {
            out.println("<table><tr><th>ID</th><th>Клиент</th><th>Дата</th><th>Статус</th>");
            out.println("<th>Сумма</th><th>Адрес</th><th>Позиции</th></tr>");
            for (CustomerOrder order : orders) {
                out.println("<tr>");
                out.println("<td>" + order.getOrderId() + "</td>");
                out.println("<td>" + escape(order.getCustomer().getName()) + "</td>");
                out.println("<td>" + order.getOrderDate().format(formatter) + "</td>");
                out.println("<td>" + escape(order.getStatus()) + "</td>");
                out.println("<td>" + order.getTotalPrice() + "</td>");
                out.println("<td>" + escape(order.getShippingAddress()) + "</td>");
                out.println("<td>");
                for (OrderDetail detail : order.getDetails()) {
                    out.println(escape(detail.getProduct().getName())
                            + " × " + detail.getQuantity() + "<br>");
                }
                out.println("</td></tr>");
            }
            out.println("</table>");
        }
        out.println("</body></html>");
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
