package ru.bsuedu.cad.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bsuedu.cad.lab.dto.ProductDTO;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.ProductService;



@WebServlet("/product/list")
public class ProductListServlet extends HttpServlet{

    private ProductService productService;

     public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.productService = context.getBean(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
            List<ProductDTO> listDTOs = productService.getProducts().stream().map(x -> new ProductDTO(x.getName(), x.getCategory().getName(), x.getStockQuantity())).collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(listDTOs);
            PrintWriter out = resp.getWriter();
            out.println(json);
    }
    
}
