package ru.bsuedu.cad.lab.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/products")
public class ProductRestServlet extends HttpServlet {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ProductRepository productRepo = (ProductRepository)
                getServletContext().getAttribute("productRepo");

        List<Product> products = productRepo.findAll();

        List<Map<String, Object>> result = products.stream().map(product -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", product.getName());
            map.put("categoryName", product.getCategory() != null ?
                    product.getCategory().getName() : "No category");
            map.put("stockQuantity", product.getStockQuantity() != null ?
                    product.getStockQuantity() : 0);
            map.put("price", product.getPrice());
            return map;
        }).toList();

        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(result));
        out.flush();
    }
}