package lab1.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab1.entity.Product;
import lab1.repository.ProductRepository;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/products")
public class ProductRestServlet extends HttpServlet {

    private ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.productRepository = context.getBean(ProductRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        List<Product> products = productRepository.findAll();

        List<Map<String, Object>> result = products.stream().map(p -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("name", p.getName());
            map.put("category", p.getCategory() != null ? p.getCategory().getName() : "Без категории");
            map.put("stock", p.getStockQuantity());
            return map;
        }).collect(Collectors.toList());

        objectMapper.writeValue(response.getOutputStream(), result);
    }
}