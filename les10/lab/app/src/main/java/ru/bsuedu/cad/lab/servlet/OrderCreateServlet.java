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
import ru.bsuedu.cad.lab.repository.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet {

  // private static final Logger logger =
  // LoggerFactory.getLogger(OrderCreateServlet.class);

  private ProductRepository productRepository;
  private CustomerRepository customerRepository;
  private OrderDetailRepository orderDetailRepository;
  private OrderService orderService;

  @Override
  public void init() throws ServletException {
    ServletContext servletContext = getServletContext();
    WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    this.orderService = context.getBean(OrderService.class);
    this.productRepository = context.getBean(ProductRepository.class);
    this.customerRepository = context.getBean(CustomerRepository.class);
    this.orderDetailRepository = context.getBean(OrderDetailRepository.class);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();
    out.println("<html><body>");
    out.println("<h1>Create order!</h1>");
    out.println("<form action='../order/create' method='post'>");
    out.println("<input type='text' name='customer_id' placeholder='Айди клиента'>");
    out.println("<input type='text' name='product_id' placeholder='Ваше имя'>");
    out.println("<input type='text' name='quantity' placeholder='Ваше имя'>");
    out.println("<input type='submit' value='Создать заказ'>"); // Вот эта строка нужна
    out.println("</form>");
    out.println("</body></html>");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Integer customer_id = Integer.parseInt(req.getParameter("customer_id"));
    Integer product_id = Integer.parseInt(req.getParameter("product_id"));
    Integer quantity = Integer.parseInt(req.getParameter("quantity"));

    Product product = (productRepository.findById(product_id)).get();
    Customer customer = (customerRepository.findById(customer_id)).get();

    // Optional<Product> productOpt = productRepository.findById(product_id);

    Order order = new Order();
    order.setCustomer(customer);
    order.setOrderDate(new Date());
    order.setStatus("Created");
    order.setShippingAddress(customer.getAddress());
    order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

    // Формируем OrderDetail
    OrderDetail detail = new OrderDetail();
    detail.setProduct(product);
    detail.setQuantity(quantity);
    detail.setPrice(product.getPrice());
    detail.setOrder(order); // связь обратная

    List<OrderDetail> orderDetails = new ArrayList<>();
    orderDetails.add(detail);

    order.setOrderDetails(orderDetails);

    orderService.createOrder(order);

    resp.sendRedirect("../order/list");
  }
}
