package ru.bsuedu.cad.lab.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet{
    final private Logger logger = LoggerFactory.getLogger(OrderListServlet.class);

    private OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orderList = orderService.getOrders();

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Форма заказа</h1>");
        out.println("<form action='create' method='post'>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID Клиента</th>");
        out.println("<td><input type='number' name='customer_id'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>ID Продукта</th>");
        out.println("<td><input type='number' name='product_id'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>Количество</th>");
        out.println("<td><input type='number' name='quantity'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td colspan='2'><input type='submit' value='Отправить'></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");

        out.println("</body></html>");
    }

    

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }



    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
    }
}
