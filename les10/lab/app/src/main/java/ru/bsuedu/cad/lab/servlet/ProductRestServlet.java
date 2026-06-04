package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProductRestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        List<Product> products = ctx.getBean(ProductRepository.class).findAll();

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("[");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            String categoryName = p.getCategory() != null ? p.getCategory().getName() : "";
            out.printf("  {\"name\":\"%s\",\"category\":\"%s\",\"stockQuantity\":%d}%s%n",
                    escape(p.getName()),
                    escape(categoryName),
                    p.getStockQuantity(),
                    i < products.size() - 1 ? "," : "");
        }
        out.println("]");
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
