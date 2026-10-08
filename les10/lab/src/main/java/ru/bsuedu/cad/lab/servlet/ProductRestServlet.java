package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.service.ProductService;

public class ProductRestServlet extends HttpServlet {

    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.productService = context.getBean(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        List<Map<String, Object>> payload = new ArrayList<>();
        for (Product product : productService.findAllProducts()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", product.getName());
            item.put("category", product.getCategory().getName());
            item.put("stockQuantity", product.getStockQuantity());
            payload.add(item);
        }
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(resp.getWriter(), payload);
    }
}
