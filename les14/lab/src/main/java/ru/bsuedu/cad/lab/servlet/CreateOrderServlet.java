package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {

    private ProductRepository productRepository;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils
            .getRequiredWebApplicationContext(getServletContext());
        productRepository = ctx.getBean(ProductRepository.class);
        orderService = ctx.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        List<Product> products = productRepository.findAll();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Создание заказа</title></head><body>");
        out.println("<h1>Создание заказа</h1>");
        out.println("<form method='POST'>");
        out.println("<label>Клиент ID: </label>");
        out.println("<input type='number' name='customerId' required><br><br>");
        out.println("<h3>Товары:</h3>");
        
        for (Product p : products) {
            out.printf("<input type='checkbox' name='productId' value='%d'> %s (%.2f руб.)<br>",
                p.getId(), p.getName(), p.getPrice());
        }
        
        out.println("<br><input type='submit' value='Создать заказ'>");
        out.println("</form>");
        out.println("<br><a href='orders'>Назад к списку заказов</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        long customerId = Long.parseLong(req.getParameter("customerId"));
        String[] productIds = req.getParameterValues("productId");
        
        List<OrderService.OrderItemData> items = new ArrayList<>();
        if (productIds != null) {
            for (String pid : productIds) {
                items.add(new OrderService.OrderItemData(Long.parseLong(pid), 1));
            }
        }
        
        orderService.createOrder(customerId, items);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}