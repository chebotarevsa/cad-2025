package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/api/products")
public class ProductRestServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(ProductRestServlet.class);
	private ProductRepository productRepository;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		this.productRepository = context.getBean(ProductRepository.class);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");

		try {
			List<Product> products = productRepository.findAll();

			String jsonResponse = objectMapper.writeValueAsString(products.stream()
					.map(p -> new ProductInfo(
							p.getName(),
							p.getCategory() != null ? p.getCategory().getName() : "Без категории",
							p.getStockQuantity()))
					.toList());

			PrintWriter out = resp.getWriter();
			out.print(jsonResponse);
			out.flush();
		} catch (Exception e) {
			logger.error("Ошибка при получении списка продуктов", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при получении списка продуктов");
		}
	}

	private static class ProductInfo {
		public final String productName;
		public final String categoryName;
		public final int stockQuantity;

		public ProductInfo(String productName, String categoryName, int stockQuantity) {
			this.productName = productName;
			this.categoryName = categoryName;
			this.stockQuantity = stockQuantity;
		}
	}
}