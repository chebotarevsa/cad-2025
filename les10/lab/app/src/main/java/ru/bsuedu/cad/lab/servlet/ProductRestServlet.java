package ru.bsuedu.cad.lab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
@WebServlet("/api/products")
public class ProductRestServlet extends HttpServlet {
    private ProductService productService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.productService = context.getBean(ProductService.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        List<Product> products = productService.getAllProducts();

        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<title>Список продуктов</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>Список продуктов</h1>");
        out.println("<table>");
        out.println("<tr><th>Название</th><th>Категория</th><th>Количество</th><th>Цена</th></tr>");

        for (Product product : products) {
            out.println("<tr>");
            out.println("<td>" + product.getName() + "</td>");
            out.println("<td>" + product.getCategory().getName() + "</td>");
            out.println("<td>" + product.getStockQuantity() + "</td>");
            out.println("<td>" + product.getPrice() + " руб.</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</body></html>");
    }
}