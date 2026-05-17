package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.service.OrderService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet("/createOrder")
public class CreateOrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Создать заказ</title></head><body>");
        out.println("<h1>Новый заказ</h1>");
        out.println("<form method='post' action='/petstore/createOrder'>");
        out.println("ID покупателя: <input type='text' name='customerId' value='1'><br>");
        out.println("ID товаров (через запятую): <input type='text' name='productIds' value='1,3'><br>");
        out.println("<input type='submit' value='Создать'>");
        out.println("</form>");
        out.println("<br><a href='/petstore/orders'>Назад к заказам</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String customerIdParam = req.getParameter("customerId");
        String productIdsParam = req.getParameter("productIds");
        int customerId = Integer.parseInt(customerIdParam);
        List<Integer> productIds = Arrays.stream(productIdsParam.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        OrderService orderService = ctx.getBean(OrderService.class);
        orderService.createOrder(customerId, productIds);

        resp.sendRedirect("/petstore/orders");
    }
}