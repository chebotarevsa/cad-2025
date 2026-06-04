package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        List<Customer> customers = ctx.getBean(CustomerRepository.class).findAll();
        List<Product> products   = ctx.getBean(ProductRepository.class).findAll();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Создать заказ</title>");
        out.println("<style>body{font-family:sans-serif;padding:20px}label{display:block;margin:8px 0 2px}");
        out.println("select,input{width:400px;padding:6px}");
        out.println(".btn{padding:8px 16px;background:#4a90d9;color:#fff;border:none;cursor:pointer;border-radius:4px;margin-top:12px}</style></head><body>");
        out.println("<h1>Создать заказ</h1>");
        out.println("<form method='post'>");
        out.println("<label>Клиент:</label><select name='customerId'>");
        for (Customer c : customers) {
            out.printf("<option value='%d'>%s</option>%n", c.getCustomerId(), c.getName());
        }
        out.println("</select>");
        out.println("<label>Товары (удерживайте Ctrl для выбора нескольких):</label>");
        out.println("<select name='productIds' multiple size='8'>");
        for (Product p : products) {
            out.printf("<option value='%d'>%s — %.2f руб.</option>%n",
                    p.getProductId(), p.getName(), p.getPrice());
        }
        out.println("</select>");
        out.println("<label>Адрес доставки:</label><input type='text' name='address' required>");
        out.println("<br><button class='btn' type='submit'>Создать заказ</button>");
        out.println("</form><br><a href='orders'>← Назад к списку заказов</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());

        int customerId = Integer.parseInt(req.getParameter("customerId"));
        String[] productIdParams = req.getParameterValues("productIds");
        String address = req.getParameter("address");

        Customer customer = ctx.getBean(CustomerRepository.class)
                .findById(customerId).orElseThrow();

        List<Product> products = List.of();
        if (productIdParams != null) {
            ProductRepository productRepo = ctx.getBean(ProductRepository.class);
            products = java.util.Arrays.stream(productIdParams)
                    .map(id -> productRepo.findById(Long.parseLong(id)).orElseThrow())
                    .collect(Collectors.toList());
        }

        ctx.getBean(OrderService.class).createOrder(customer, products, address);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
