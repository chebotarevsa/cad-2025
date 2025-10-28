package ru.bsuedu.cad.lab.servlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet {

  // private static final Logger logger =
  // LoggerFactory.getLogger(OrderCreateServlet.class);

  private OrderService orderService;

  @Override
  public void init() throws ServletException {
    ServletContext servletContext = getServletContext();
    WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    this.orderService = context.getBean(OrderService.class);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    List<Order> orderlist = orderService.getAllOrders();

    resp.setContentType("text/html; charset=UTF-8");
    resp.setCharacterEncoding("UTF-8");
    PrintWriter out = resp.getWriter();
    out.println("<html><head><meta charset='UTF-8'></head><body>");
    out.println("<h1>Order list with details!</h1>");
    for (Order order : orderlist) {
      out.println("<p>");
      out.println("ID: " + order.getOrderId() + "<br>");
      out.println("Order Date: " + order.getOrderDate() + "<br>");
      out.println("Total Price: " + order.getTotalPrice() + "<br>");
      out.println("Status:" + order.getStatus() + "<br>");
      out.println("Shipping Address: " + order.getShippingAddress() + "<br>");
      out.println("Customer: " + order.getCustomer() + "<br>");
      out.println("</p>");
    }

    out.println("<p>--- DEBUG: конец списка заказов ---</p>");
    out.println("<a href='" + req.getContextPath() + "/order/create'><button>Go to create order</button></a>");
    out.println("</body></html>");
  }
}
