package ru.bsuedu.cad.lab.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductsApiServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json; charset=UTF-8");

        ProductRepository productRepository = WebUtils.getBean(getServletContext(), ProductRepository.class);
        List<ProductInfo> products = productRepository.findAllWithCategory().stream()
                .map(product -> new ProductInfo(
                        product.getName(),
                        product.getCategory().getName(),
                        product.getStockQuantity()))
                .toList();

        objectMapper.writeValue(response.getWriter(), products);
    }

    public record ProductInfo(String productName, String categoryName, Integer stockQuantity) {
    }
}
