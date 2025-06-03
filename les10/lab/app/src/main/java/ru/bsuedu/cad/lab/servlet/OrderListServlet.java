package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.bsuedu.cad.lab.entity.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();
        try {
            List<CustomerOrder> orders = em.createQuery(
                "SELECT DISTINCT o FROM CustomerOrder o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.product ORDER BY o.orderDate DESC", 
                CustomerOrder.class)
                .getResultList();

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();

            out.println("""
                <html>
                <head>
                    <title>Список заказов</title>
                    <style>
                        table { border-collapse: collapse; width: 100%; }
                        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                        th { background-color: #f2f2f2; }
                        .order-header { background-color: #e6f7ff; }
                        .total-row { font-weight: bold; background-color: #e6ffe6; }
                        .btn { 
                            padding: 8px 16px;
                            background-color: #4CAF50;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                            border: none;
                            cursor: pointer;
                            margin-top: 20px;
                        }
                    </style>
                </head>
                <body>
                    <h1>Список заказов</h1>
                    <table>
                        <tr>
                            <th>ID заказа</th>
                            <th>Клиент</th>
                            <th>Дата</th>
                            <th>Статус</th>
                            <th>Адрес доставки</th>
                            <th>Сумма</th>
                        </tr>
                """);

            for (CustomerOrder order : orders) {
                out.printf("""
                    <tr class="order-header">
                        <td>%d</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%.2f руб.</td>
                    </tr>
                    """,
                    order.getId(),
                    order.getCustomer().getName(),
                    order.getOrderDate(),
                    order.getStatus(),
                    order.getShippingAddress(),
                    order.getTotalPrice());
            }

            out.println("""
                    </table>
                    <form action='create-order' method='get'>
                        <button type='submit' class='btn'>Создать новый заказ</button>
                    </form>
                </body>
                </html>
                """);
        } finally {
            em.close();
        }
    }
}