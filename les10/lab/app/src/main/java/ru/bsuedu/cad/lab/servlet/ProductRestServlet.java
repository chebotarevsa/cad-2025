package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/products")
public class ProductRestServlet extends HttpServlet {

  private ProductRepository productRepository;

  @Override
  public void init() throws ServletException {
    ServletContext servletContext = getServletContext();
    WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    this.productRepository = context.getBean(ProductRepository.class);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<Product> products = productRepository.findAll();

    resp.setContentType("application/json; charset=UTF-8");
    resp.setCharacterEncoding("UTF-8");

    PrintWriter out = resp.getWriter();
    out.println("[");
    for (int i = 0; i < products.size(); i++) {
      Product p = products.get(i);
      out.print("  {");
      out.print("\"name\": \"" + escape(p.getName()) + "\", ");
      out.print("\"category\": \"" + escape(p.getCategory().getName()) + "\", ");
      out.print("\"stock\": " + p.getStockQuantity());
      out.print("}");
      if (i < products.size() - 1)
        out.println(",");
      else
        out.println();
    }
    out.println("]");
  }

  private String escape(String text) {
    return text == null ? "" : text.replace("\"", "\\\""); // минимальная защита
  }
}