package ru.bsuedu.cad.lab.web;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.*;
import org.springframework.util.StreamUtils;
import ru.bsuedu.cad.lab.App;
import ru.bsuedu.cad.lab.app.DataLoader;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@WebServlet("/api/products")
public class ProductRestServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        var ctx = new org.springframework.context.annotation
                .AnnotationConfigApplicationContext(App.class);

        try {
            ctx.getBean(DataLoader.class).run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        productRepository = ctx.getBean(ProductRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json;charset=UTF-8");

        Iterable<Product> products = productRepository.findAll();
        resp.getWriter().write(toJsonList(StreamSupport.stream(products.spliterator(), false).toList()));

    }

    private String toJson(Product p) {
        if (p == null) return "null";

        return "{"
                + "\"id\":" + p.getProductId() + ","
                + "\"name\":\"" + p.getName() + "\","
                + "\"description\":\"" + p.getDescription() + "\","
                + "\"stock\":" + p.getStockQuantity()
                + "}";
    }

    private String toJsonList(List<Product> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < list.size(); i++) {
            sb.append(toJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }

        sb.append("]");
        return sb.toString();
    }
}