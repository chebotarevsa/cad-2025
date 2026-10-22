package lab1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab1.entity.Order;
import lab1.service.OrderService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<Order> orders = orderService.getAllOrders();

        out.println("""
            <!DOCTYPE html>
            <html lang="ru">
            <head>
                <meta charset="UTF-8">
                <title>Заказы зоомагазина</title>
            </head>
            <body>
                <h1>Список заказов</h1>
                <form action="create-order" method="get">
                    <button type="submit">Создать заказ</button>
                </form>
                <br>
                <table border="1" cellpadding="5" cellspacing="0">
                    <tr>
                        <th>ID</th>
                        <th>Клиент</th>
                        <th>Дата</th>
                        <th>Статус</th>
                        <th>Сумма</th>
                    </tr>
            """);

        for (Order o : orders) {
            out.println("<tr>");
            out.println("<td>" + o.getOrderId() + "</td>");
            out.println("<td>" + (o.getCustomer() != null ? o.getCustomer().getName() : "Неизвестно") + "</td>");
            out.println("<td>" + o.getOrderDate() + "</td>");
            out.println("<td>" + o.getStatus() + "</td>");
            out.println("<td>" + o.getTotalPrice() + "</td>");
            out.println("</tr>");
        }

        out.println("""
                </table>
            </body>
            </html>
            """);
    }
}