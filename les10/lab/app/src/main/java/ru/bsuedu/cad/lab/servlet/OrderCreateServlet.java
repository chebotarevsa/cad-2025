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

@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet{

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
        out.println("<h1>Create order</h1>");
        out.println("<form action='create' method='post'><label for='customerID'>Enter customer ID:</label><input type='text' name='customerID' id='customerID' required/><br/>" + 
        "<label for='productID'>Enter product ID:</label> <input type='text' name='productID' id='productID' required /><br/>" + 
        "<label for='quantity'>Enter product quantity:</label> <input type='text' name='quantity' id='quantity' required /><br/>" + 
        "<button type='submit'>Submit</button></form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        int customerID = Integer.parseInt(req.getParameter("customerID"));
        int productID = Integer.parseInt(req.getParameter("productID"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        orderService.createOrder(customerID, productID, quantity);
        resp.sendRedirect("list");
    }
    
    
}
