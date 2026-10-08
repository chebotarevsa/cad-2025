package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.service.CustomerService;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.ProductService;

public class OrderCreateServlet extends HttpServlet {

    private OrderService orderService;
    private CustomerService customerService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
        this.customerService = context.getBean(CustomerService.class);
        this.productService = context.getBean(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        renderForm(req, resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            Integer customerId = Integer.valueOf(req.getParameter("customerId"));
            Integer productId = Integer.valueOf(req.getParameter("productId"));
            Integer quantity = Integer.valueOf(req.getParameter("quantity"));
            orderService.createOrder(customerId, productId, quantity);
            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (Exception e) {
            renderForm(req, resp, e.getMessage());
        }
    }

    private void renderForm(HttpServletRequest req, HttpServletResponse resp, String error)
            throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        List<Customer> customers = customerService.findAllCustomers();
        List<Product> products = productService.findAllProducts();
        String ctx = req.getContextPath();

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='ru'><head><meta charset='UTF-8'>");
        out.println("<title>Создание заказа</title>");
        out.println("<style>");
        out.println("body{font-family:Segoe UI,Arial,sans-serif;margin:24px;background:#f7f4ef;color:#222}");
        out.println("form{background:#fff;padding:16px;border:1px solid #ddd;max-width:520px}");
        out.println("label{display:block;margin:10px 0 4px}");
        out.println("select,input{width:100%;padding:8px;box-sizing:border-box}");
        out.println("button{margin-top:14px;padding:10px 16px;background:#5b3a8c;color:#fff;border:0;border-radius:6px}");
        out.println(".err{color:#b00020;margin-bottom:10px}");
        out.println("</style></head><body>");
        out.println("<h1>Создание заказа</h1>");
        out.println("<p><a href='" + ctx + "/orders'>← К списку заказов</a></p>");
        if (error != null) {
            out.println("<div class='err'>" + escape(error) + "</div>");
        }
        out.println("<form method='post' action='" + ctx + "/orders/new'>");
        out.println("<label>Клиент</label><select name='customerId' required>");
        for (Customer customer : customers) {
            out.println("<option value='" + customer.getCustomerId() + "'>"
                    + escape(customer.getName()) + "</option>");
        }
        out.println("</select>");
        out.println("<label>Товар</label><select name='productId' required>");
        for (Product product : products) {
            out.println("<option value='" + product.getProductId() + "'>"
                    + escape(product.getName()) + " (остаток: " + product.getStockQuantity()
                    + ", цена: " + product.getPrice() + ")</option>");
        }
        out.println("</select>");
        out.println("<label>Количество</label>");
        out.println("<input type='number' name='quantity' min='1' value='1' required>");
        out.println("<button type='submit'>Создать заказ</button>");
        out.println("</form></body></html>");
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
