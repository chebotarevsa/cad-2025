package lab1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab1.service.OrderService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/create-order")
public class OrderCreateServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Создание заказа</title>
                </head>
                <body>
                    <h1>Создать заказ</h1>
                    <form action="create-order" method="post">
                        <label>ID Клиента: <input type="number" name="customerId" required></label><br><br>
                        <label>ID Товара: <input type="number" name="productId" required></label><br><br>
                        <label>Количество: <input type="number" name="quantity" min="1" value="1" required></label><br><br>
                        <label>Адрес доставки: <input type="text" name="shippingAddress" required></label><br><br>
                        <button type="submit">Оформить</button>
                    </form>
                    <br>
                    <a href="orders">Назад к списку заказов</a>
                </body>
                </html>
                """);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Long customerId = Long.parseLong(request.getParameter("customerId"));
            Long productId = Long.parseLong(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String shippingAddress = request.getParameter("shippingAddress");

            OrderService.OrderItemRequest itemRequest = new OrderService.OrderItemRequest();
            itemRequest.setProductId(productId);
            itemRequest.setQuantity(quantity);

            List<OrderService.OrderItemRequest> items = List.of(itemRequest);

            orderService.createOrder(customerId, items, shippingAddress);

            response.sendRedirect("orders");

        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<h1>Ошибка при создании заказа</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='create-order'>Попробовать снова</a>");
        }
    }
}