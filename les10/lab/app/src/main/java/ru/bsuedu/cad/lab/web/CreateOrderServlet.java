package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderItem;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CreateOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showForm(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            Integer customerId = Integer.parseInt(request.getParameter("customerId"));
            Integer productId = Integer.parseInt(request.getParameter("productId"));
            Integer quantity = Integer.parseInt(request.getParameter("quantity"));
            String address = request.getParameter("shippingAddress");

            OrderService orderService = WebUtils.getBean(getServletContext(), OrderService.class);
            orderService.createOrder(customerId, List.of(new OrderItem(productId, quantity)), address);

            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (Exception e) {
            showForm(request, response, "Не удалось создать заказ: " + e.getMessage());
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String error)
            throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");

        CustomerRepository customerRepository = WebUtils.getBean(getServletContext(), CustomerRepository.class);
        ProductRepository productRepository = WebUtils.getBean(getServletContext(), ProductRepository.class);
        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAllWithCategory();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!doctype html>");
            out.println("<html lang='ru'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Создание заказа</title>");
            out.println("<style>");
            out.println("body{font-family:Arial,sans-serif;margin:20px;}");
            out.println("label{display:block;margin-top:10px;}");
            out.println(".error{color:red;}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Создание заказа</h1>");
            out.println("<p><a href='" + request.getContextPath() + "/orders'>Вернуться к списку заказов</a></p>");

            if (error != null) {
                out.println("<div class='error'>" + WebUtils.html(error) + "</div>");
            }

            out.println("<form method='post'>");
            out.println("<label>Клиент</label>");
            out.println("<select name='customerId'>");
            for (Customer customer : customers) {
                out.println("<option value='" + customer.getId() + "'>"
                        + WebUtils.html(customer.getName()) + "</option>");
            }
            out.println("</select>");

            out.println("<label>Товар</label>");
            out.println("<select name='productId'>");
            for (Product product : products) {
                out.println("<option value='" + product.getId() + "'>"
                        + WebUtils.html(product.getName())
                        + " (" + product.getPrice() + " руб., на складе "
                        + product.getStockQuantity() + ")</option>");
            }
            out.println("</select>");

            out.println("<label>Количество</label>");
            out.println("<input type='number' name='quantity' value='1' min='1' required>");

            out.println("<label>Адрес доставки</label>");
            out.println("<input type='text' name='shippingAddress' required>");

            out.println("<button type='submit'>Создать заказ</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
