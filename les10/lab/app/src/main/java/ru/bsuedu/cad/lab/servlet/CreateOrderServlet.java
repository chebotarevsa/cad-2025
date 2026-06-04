package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderLineRequest;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.web.HtmlEscape;

public class CreateOrderServlet extends HttpServlet {

    private OrderService orderService;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        orderService = context.getBean(OrderService.class);
        customerRepository = context.getBean(CustomerRepository.class);
        productRepository = context.getBean(ProductRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html lang=\"ru\"><head><meta charset=\"UTF-8\">");
        out.println("<title>Новый заказ</title></head><body>");
        out.println("<h1>Создание заказа</h1>");
        out.println("<form method=\"post\" action=\"" + req.getContextPath() + "/orders/new\">");

        out.println("<p><label>Клиент: <select name=\"customerId\" required>");
        for (Customer customer : customerRepository.findAll()) {
            out.println("<option value=\"" + customer.getCustomerId() + "\">");
            out.print(HtmlEscape.escape(customer.getName()));
            out.println("</option>");
        }
        out.println("</select></label></p>");

        out.println("<p><label>Адрес доставки: <input type=\"text\" name=\"shippingAddress\" required></label></p>");

        out.println("<p><label>Товар: <select name=\"productId\" required>");
        for (Product product : productRepository.findAll()) {
            out.println("<option value=\"" + product.getProductId() + "\">");
            out.print(HtmlEscape.escape(product.getName()));
            out.println("</option>");
        }
        out.println("</select></label></p>");

        out.println("<p><label>Количество: <input type=\"number\" name=\"quantity\" min=\"1\" value=\"1\" required></label></p>");
        out.println("<p><button type=\"submit\">Создать</button></p>");
        out.println("</form>");
        out.println("<p><a href=\"" + req.getContextPath() + "/orders\">К списку заказов</a></p>");
        out.println("</body></html>");
        resp.flushBuffer();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int customerId = Integer.parseInt(req.getParameter("customerId"));
        String shippingAddress = req.getParameter("shippingAddress");
        long productId = Long.parseLong(req.getParameter("productId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        orderService.createOrder(
                customerId, shippingAddress, List.of(new OrderLineRequest(productId, quantity)));

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
