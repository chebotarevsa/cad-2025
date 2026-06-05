package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.entity.OrderItem;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        OrderRepository orderRepo = (OrderRepository)
                getServletContext().getAttribute("orderRepo");

        List<OrderEntity> orders = orderRepo.findAll();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Order List</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("tr:hover { background-color: #f5f5f5; }");
        out.println("button { background-color: #4CAF50; color: white; padding: 10px 20px; ");
        out.println("border: none; cursor: pointer; font-size: 16px; }");
        out.println("button:hover { background-color: #45a049; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Order List</h1>");

        if (orders.isEmpty()) {
            out.println("<p>No orders yet</p>");
        } else {
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Created Date</th>");
            out.println("<th>Customer</th>");
            out.println("<th>Items</th>");
            out.println("<th>Total Amount</th>");
            out.println("</tr>");

            for (OrderEntity order : orders) {
                // Calculate total amount
                double totalAmount = 0;
                StringBuilder itemsInfo = new StringBuilder();

                if (order.getItems() != null) {
                    for (OrderItem item : order.getItems()) {
                        double itemTotal = item.getQuantity() * item.getProduct().getPrice().doubleValue();
                        totalAmount += itemTotal;
                        itemsInfo.append(item.getProduct().getName())
                                .append(" (x").append(item.getQuantity()).append("), ");
                    }
                    if (itemsInfo.length() > 2) {
                        itemsInfo.setLength(itemsInfo.length() - 2);
                    }
                }

                out.println("<tr>");
                out.println("<td>" + order.getId() + "</td>");
                out.println("<td>" + (order.getCreatedAt() != null ? order.getCreatedAt() : "N/A") + "</td>");
                out.println("<td>" + (order.getCustomer() != null ? order.getCustomer().getName() : "No customer") + "</td>");
                out.println("<td>" + (itemsInfo.length() > 0 ? itemsInfo.toString() : "No items") + "</td>");
                out.println("<td>" + totalAmount + " rub." + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("<br>");
        out.println("<a href='create-order'><button>Create New Order</button></a>");

        out.println("</body>");
        out.println("</html>");
    }
}