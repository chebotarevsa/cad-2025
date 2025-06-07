package ru.bsuedu.cad.lab.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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


@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet{

    final private Logger logger = LoggerFactory.getLogger(OrderListServlet.class);

    private OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orderList = orderService.getOrders();

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><head><meta charset='utf-8'></head><body>");
        out.println("<h1>Hello from raw Servlet!</h1>");
        out.println("<a href='../order/create'>Создать<button></button></a>");
        out.println("</body></html>");
    }

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
    }
    
}
