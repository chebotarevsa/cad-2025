package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
import ru.bsuedu.cad.lab.service.OrderService;

@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreateServlet.class);

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><meta charset='UTF-8'><title>Создать заказ</title></head><body>");
        out.println("<h1>Форма создания заказа</h1>");
        out.println("<form action='" + req.getContextPath() + "/order/create' method='post'>");
        out.println("<table>");
        out.println("<tr><th>ID Клиента:</th><td><input type='number' name='customer_id' required></td></tr>");
        out.println("<tr><th>ID Продукта:</th><td><input type='number' name='product_id' required></td></tr>");
        out.println("<tr><th>Количество:</th><td><input type='number' name='quantity' min='1' required></td></tr>");
        out.println("<tr><td colspan='2'><input type='submit' value='Создать заказ'></td></tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            Long customerId = Long.parseLong(req.getParameter("customer_id"));
            Long productId = Long.parseLong(req.getParameter("product_id"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            orderService.createOrder(customerId, productId, quantity);

            resp.sendRedirect(req.getContextPath() + "/order/list");
        } catch (Exception e) {
            logger.error("Ошибка при создании заказа", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные данные");
        }
    }
}
