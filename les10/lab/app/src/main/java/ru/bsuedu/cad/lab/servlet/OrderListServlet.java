package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
import ru.bsuedu.cad.lab.service.ProductService;

@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet{

    private OrderService orderService;

     public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
            var orders = orderService.getOrders();
            PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Orders: </h1>");
        for (Order order : orders) {
            out.println("<h2>" + order.toString() + "</h2>");
        }
        out.println("<a href='create'><button>Create</button></a>");
        out.println("</body></html>");
    }
    
}
