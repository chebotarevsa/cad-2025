package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.App;
import ru.bsuedu.cad.lab.app.DataLoader;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.OrderDetailRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {

    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var ctx = new org.springframework.context.annotation
                .AnnotationConfigApplicationContext(App.class);

        try {
            ctx.getBean(DataLoader.class).run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.orderRepository = ctx.getBean(OrderRepository.class);
        this.orderDetailRepository = ctx.getBean(OrderDetailRepository.class);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html;charset=UTF-8");

        String html =
                "<h1>Create Order</h1>" +
                        "<form method='post'>" +

                        "Customer ID: <input name='customerId'/><br/>" +
                        "Product ID: <input name='productId'/><br/>" +
                        "Quantity: <input name='quantity'/><br/>" +
                        "Shipping Address: <input name='shippingAddress'/><br/>" +

                        "<button type='submit'>Create Order</button>" +
                        "</form>";

        resp.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Integer customerId = Integer.parseInt(req.getParameter("customerId"));
        Integer productId = Integer.parseInt(req.getParameter("productId"));
        Integer quantity = Integer.parseInt(req.getParameter("quantity"));
        String shippingAddress = req.getParameter("shippingAddress");

        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Category category = new Category();
        category.setCategoryId(1);

        Product product = new Product();
        product.setProductId(productId);
        product.setCategory(category);

        OrderDetail detail = new OrderDetail();
        detail.setOrderDetailId(1);
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setPrice(product.getPrice());
        orderDetailRepository.save(detail);

        List<OrderDetail> details = new ArrayList<>();
        details.add(detail);

        Order order = new Order();
        order.setOrderId(1);
        order.setCustomer(customer);
        order.setOrderDetails(details);
        order.setShippingAddress(shippingAddress);
        order.setStatus("NEW");
        order.setOrderDate(java.time.LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        if (product.getPrice() != null) {
            total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
        order.setTotalPrice(total);

        orderRepository.save(order);

        resp.sendRedirect("/orders");
    }
}