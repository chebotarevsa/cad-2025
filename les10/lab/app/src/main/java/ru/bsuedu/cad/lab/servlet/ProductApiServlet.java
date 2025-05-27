package ru.bsuedu.cad.lab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/products")
public class ProductApiServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.productRepository = context.getBean(ProductRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        List<Product> products = productRepository.findAll();

        List<ProductDTO> dtos = products.stream()
                .map(product -> new ProductDTO(
                        product.getName(),
                        product.getCategory().getName(),
                        product.getStockQuantity()
                ))
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getOutputStream(), dtos);
    }

    static class ProductDTO {
        public String name;
        public String category;
        public int stockQuantity;

        public ProductDTO(String name, String category, int stockQuantity) {
            this.name = name;
            this.category = category;
            this.stockQuantity = stockQuantity;
        }
    }
}
