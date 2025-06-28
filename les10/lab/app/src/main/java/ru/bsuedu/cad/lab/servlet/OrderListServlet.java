package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        List<Order> orders = orderService.getAllOrders();

        out.println("<html><head>");
        out.println("<title>Список заказов</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("a { display: inline-block; margin-top: 20px; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>Список заказов</h1>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Покупатель</th><th>Дата</th><th>Сумма</th><th>Статус</th></tr>");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (Order order : orders) {
            out.println("<tr>");
            out.println("<td>" + order.getId() + "</td>");
            out.println("<td>" + order.getCustomer().getName() + "</td>");
            out.println("<td>" + order.getOrderDate().format(formatter) + "</td>");
            out.println("<td>" + order.getTotalPrice() + " руб.</td>");
            out.println("<td>" + order.getStatus() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<a href='" + req.getContextPath() + "/create-order'>Создать новый заказ</a>");
        out.println("</body></html>");
    }
}