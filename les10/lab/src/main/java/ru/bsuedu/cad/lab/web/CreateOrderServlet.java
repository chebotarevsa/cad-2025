package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        CustomerRepository customerRepo = (CustomerRepository)
                getServletContext().getAttribute("customerRepo");
        ProductRepository productRepo = (ProductRepository)
                getServletContext().getAttribute("productRepo");

        List<Customer> customers = customerRepo.findAll();
        List<Product> products = productRepo.findAll();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Create Order</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>New Order</h2>");
        out.println("<form method='post'>");

        out.println("<label>Customer:</label>");
        out.println("<select name='customerId'>");
        for (Customer customer : customers) {
            out.println("<option value='" + customer.getId() + "'>" + customer.getName() + "</option>");
        }
        out.println("</select><br>");

        out.println("<label>Product:</label>");
        out.println("<select name='productId'>");
        for (Product product : products) {
            out.println("<option value='" + product.getId() + "'>" + product.getName() + "</option>");
        }
        out.println("</select><br>");

        out.println("<label>Quantity:</label>");
        out.println("<input type='number' name='quantity' min='1'><br>");

        out.println("<input type='submit' value='Place order'>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long customerId = Long.parseLong(req.getParameter("customerId"));
        Long productId = Long.parseLong(req.getParameter("productId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        OrderService orderService = (OrderService)
                getServletContext().getAttribute("orderService");

        orderService.createOrder(customerId, productId, quantity);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}