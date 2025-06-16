package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet{
	final private Logger logger = LoggerFactory.getLogger(OrderListServlet.class);

	private OrderService orderService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Order> orderList = orderService.getAllOrders();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><head><meta charset='utf-8'><link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css'></head><body>");
        out.println("<h1>List of orders</h1>");
        out.println("<a href='" + req.getContextPath() + "/order/create'><button>Create an order</button></a>");
    for (Order order : orderList) {
      out.println("<p>");
      out.println("ID: " + order.getOrderId() + "");
      out.println("Order Date: " + order.getOrderDate() + "");
      out.println("Total Price: " + order.getTotalPrice() + "");
      out.println("Status:" + order.getStatus() + "");
      out.println("Shipping Address: " + order.getShippingAddress() + "");
      out.println("Customer: " + order.getCustomer() + "");
      out.println("</p>");
    }

    //out.println("<p>--- DEBUG: конец списка заказов ---</p>");
        out.println("</body></html>");
        
	}
	
	
	
}
