package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.OrderService;

@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet{
	final private Logger logger = LoggerFactory.getLogger(OrderCreateServlet.class);

	private OrderService orderService;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

     @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
        this.customerRepository = context.getBean(CustomerRepository.class);
        this.productRepository = context.getBean(ProductRepository.class);

    }


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Order> orderList = orderService.getAllOrders();

		resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><head><link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css'></head><body>");
        out.println("<h1>Order form</h1>");
        out.println("<form action='create' method='post'>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID Client</th>");
        out.println("<td><input type='number' name='customer_id'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>ID Product</th>");
        out.println("<td><input type='number' name='product_id'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>Quantity</th>");
        out.println("<td><input type='number' name='quantity'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td colspan='2'><input type='submit' value='Send'></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</body></html>");
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Integer customer_id = Integer.parseInt(req.getParameter("customer_id"));
    Integer product_id = Integer.parseInt(req.getParameter("product_id"));
    Integer quantity = Integer.parseInt(req.getParameter("quantity"));

    orderService.createOrder(customer_id, product_id, quantity);

    resp.sendRedirect("../order/list");
    }
	
	
	
}
