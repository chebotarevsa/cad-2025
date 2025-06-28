package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.service.CustomerService;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {
    private OrderService orderService;
    private ProductService productService;
    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
        this.productService = context.getBean(ProductService.class);
        this.customerService = context.getBean(CustomerService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head>");
        out.println("<title>Создание заказа</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".product-item { margin: 10px 0; padding: 10px; border: 1px solid #eee; }");
        out.println("input[type='submit'] { padding: 8px 15px; background: #4CAF50; color: white; border: none; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>Создание нового заказа</h1>");
        out.println("<form method='post'>");
        out.println("<h3>Выберите товары:</h3>");

        for (Product product : productService.getAllProducts()) {
            out.println("<div class='product-item'>");
            out.println("<label>");
            out.println("<input type='checkbox' name='productIds' value='" + product.getId() + "'>");
            out.println(product.getName() + " (Цена: " + product.getPrice() + " руб.)");
            out.println("</label>");
            out.println("<br>Количество: <input type='number' name='quantity_" + product.getId() + "' min='1' value='1'>");
            out.println("</div>");
        }

        out.println("<input type='submit' value='Создать заказ'>");
        out.println("</form>");
        out.println("<a href='" + req.getContextPath() + "/orders'>← Назад к списку заказов</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String[] productIds = req.getParameterValues("productIds");
            if (productIds == null || productIds.length == 0) {
                resp.sendRedirect(req.getContextPath() + "/create-order?error=no_products");
                return;
            }

            Customer customer = customerService.getAllCustomers().get(0);
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (String productId : productIds) {
                long id = Long.parseLong(productId);
                Product product = productService.getProductById(id);
                int quantity = Integer.parseInt(req.getParameter("quantity_" + productId));

                OrderDetail detail = new OrderDetail();
                detail.setProduct(product);
                detail.setQuantity(quantity);
                detail.setPrice(product.getPrice());
                orderDetails.add(detail);
            }

            orderService.createOrder(customer, orderDetails, customer.getAddress());
            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (Exception e) {

            resp.sendRedirect(req.getContextPath() + "/create-order?error=server_error");
        }
    }
}