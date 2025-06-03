package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.bsuedu.cad.lab.entity.CustomerOrder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


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
        List<CustomerOrder> orders = em.createQuery("SELECT o FROM CustomerOrder o", CustomerOrder.class)
                                       .getResultList();
        em.close();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Список заказов</title></head><body>");
        out.println("<h1>Список заказов</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Клиент</th><th>Дата</th><th>Сумма</th><th>Статус</th></tr>");

        for (CustomerOrder order : orders) {
            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    order.getId(),
                    order.getCustomer().getName(),
                    order.getOrderDate(),
                    order.getTotalPrice(),
                    order.getStatus());
        }

        out.println("</table>");
        out.println("<form action='" + req.getContextPath() + "/create-order' method='get'>");
        out.println("<button type='submit'>Создать заказ</button>");
        out.println("</form>");
        out.println("</body></html>");
    }
}