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
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(OrderCreateServlet.class);
	private OrderService orderService;

	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		this.orderService = context.getBean(OrderService.class);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>Создание заказа</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Создание нового заказа</h1>");
		out.println("<form method=\"post\">");
		out.println("ID клиента: <input type=\"number\" name=\"customerId\" required><br/>");
		out.println("ID продукта: <input type=\"number\" name=\"productId\" required><br/>");
		out.println("Количество: <input type=\"number\" name=\"quantity\" required><br/>");
		out.println("<input type=\"submit\" value=\"Создать заказ\">");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html;charset=UTF-8");
			int customerId = Integer.parseInt(req.getParameter("customerId"));
			int productId = Integer.parseInt(req.getParameter("productId"));
			int quantity = Integer.parseInt(req.getParameter("quantity"));

			orderService.createOrder(customerId, productId, quantity);
			logger.info("Успешно создан заказ для клиента ID: {}, продукта ID: {}, количество: {}",
					customerId, productId, quantity);
			resp.sendRedirect(req.getContextPath() + "/order/list");
		} catch (Exception e) {
			logger.error("Ошибка создания заказа", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка создания заказа: " + e.getMessage());
		}
	}
}
