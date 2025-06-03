package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@WebServlet("/create-order")
public class OrderFormServlet extends HttpServlet {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Создание заказа</title></head><body>");
        out.println("<h1>Создать заказ</h1>");
        out.println("<form action='" + req.getContextPath() + "/create-order' method='post'>");
        out.println("ID клиента: <input type='text' name='customerId'><br/>");
        out.println("ID товаров (через запятую): <input type='text' name='productIds'><br/>");
        out.println("<button type='submit'>Создать</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int customerId = Integer.parseInt(req.getParameter("customerId"));
        List<Integer> productIds = Arrays.stream(req.getParameter("productIds").split(","))
                                         .map(String::trim)
                                         .map(Integer::parseInt)
                                         .toList();

        try {

            Customer customer = customerRepository.findById(customerId).orElse(null);
            if (customer == null) {
                throw new IllegalArgumentException("Клиент не найден");
            }

            BigDecimal total = BigDecimal.ZERO;
            for (Integer pid : productIds) {
                Product p = productRepository.findById(pid).orElse(null);
                if (p != null) {
                    total = total.add(p.getPrice());
                } else {
                    System.err.println("⚠️ Товар с ID " + pid + " не найден.");
                }
            }

            CustomerOrder order = new CustomerOrder();
            order.setCustomer(customer);
            order.setOrderDate(LocalDateTime.now());
            order.setTotalPrice(total);
            order.setStatus("Создан");
            order.setShippingAddress(customer.getAddress());
            orderRepository.save(order);


        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Ошибка при создании заказа", e);
        }

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
