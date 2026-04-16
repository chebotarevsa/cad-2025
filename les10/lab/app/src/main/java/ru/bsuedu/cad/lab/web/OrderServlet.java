package ru.bsuedu.cad.lab.web;

import jakarta.persistence.Id;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.bsuedu.cad.lab.App;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.io.IOException;

@Slf4j
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private OrderRepository orderRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var ctx = new org.springframework.context.annotation
                .AnnotationConfigApplicationContext(App.class);

        this.orderRepository = ctx.getBean(OrderRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder();

        html.append("<h1>Orders</h1>");

        html.append("<ul>");
        Iterable<Order> orders = orderRepository.findAll();
        log.info(orders.toString());
        for (Order o : orders) {

            html.append("<li>")
                    .append("ID: ").append(o.getOrderId())
                    .append(", Product: ").append(o.getOrderDetails().get(0).getProduct())
                    .append(", Quantity: ").append(o.getOrderDetails().get(0).getQuantity())
                    .append("</li>");
        }
        html.append("</ul>");

        html.append("<a href='/create-order'>Create order</a>");

        resp.getWriter().write(html.toString());
    }
}
