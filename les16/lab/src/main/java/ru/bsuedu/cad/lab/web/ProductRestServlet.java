package ru.bsuedu.cad.lab.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/api/products")
public class ProductRestServlet extends HttpServlet {

    private ProductRepository productRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.productRepository = ctx.getBean(ProductRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        var products = productRepository.findAll();
        List<Map<String, Object>> out = products.stream().map(p -> {
            var m = new java.util.HashMap<String, Object>();
            m.put("name", p.getName());
            m.put("category", p.getCategory() != null ? p.getCategory().getName() : null);
            m.put("stock", p.getStockQuantity());
            return m;
        }).collect(Collectors.toList());

        mapper.writeValue(resp.getOutputStream(), out);
    }
}
