package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/create-order")
public class OrderFormServlet extends HttpServlet {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Product> products = productRepository.findAll();
        List<Customer> customers = customerRepository.findAll();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("""
            <html>
            <head>
                <title>Создание заказа</title>
                <style>
                    .product-item { margin-bottom: 10px; padding: 10px; border: 1px solid #ddd; }
                    .add-btn { margin-left: 10px; }
                    #selected-products { margin: 20px 0; }
                </style>
                <script>
                    function addProduct(productId, productName, price) {
                        const container = document.getElementById('selected-products');
                        const div = document.createElement('div');
                        div.className = 'product-item';
                        div.innerHTML = `
                            <input type="hidden" name="productIds" value="${productId}">
                            ${productName} (Цена: ${price})
                            Количество: <input type="number" name="quantities" min="1" value="1">
                            <button type="button" onclick="this.parentNode.remove()">Удалить</button>
                        `;
                        container.appendChild(div);
                    }
                </script>
            </head>
            <body>
                <h1>Создать новый заказ</h1>
                <form method='post'>
                    <label for='customerId'>Клиент:</label>
                    <select id='customerId' name='customerId' required>
            """);

        for (Customer customer : customers) {
            out.printf("<option value='%d'>%s (ID: %d)</option>",
                    customer.getId(), customer.getName(), customer.getId());
        }

        out.println("""
                    </select>
                    
                    <h3>Доступные товары:</h3>
            """);

        for (Product product : products) {
            out.printf("""
                <div class='product-item'>
                    %s (ID: %d, Цена: %.2f, Остаток: %d)
                    <button type='button' class='add-btn' 
                        onclick='addProduct(%d, "%s", %.2f)'>
                        Добавить в заказ
                    </button>
                </div>
                """,
                product.getName(), product.getId(), product.getPrice(), product.getStockQuantity(),
                product.getId(), product.getName(), product.getPrice());
        }

        out.println("""
                    <h3>Выбранные товары:</h3>
                    <div id="selected-products"></div>
                    
                    <button type='submit'>Создать заказ</button>
                </form>
            </body>
            </html>
            """);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(req.getParameter("customerId"));
            String[] productIds = req.getParameterValues("productIds");
            String[] quantities = req.getParameterValues("quantities");

            if (productIds == null || productIds.length == 0) {
                throw new IllegalArgumentException("Не выбрано ни одного товара");
            }

            Map<Integer, Integer> productQuantities = new HashMap<>();
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                productQuantities.put(productId, quantity);
            }

            orderService.createOrder(customerId, productQuantities);
            resp.sendRedirect(req.getContextPath() + "/orders");

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                         "Ошибка создания заказа: " + e.getMessage());
        }
    }
}