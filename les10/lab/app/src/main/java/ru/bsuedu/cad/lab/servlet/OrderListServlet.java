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

import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(OrderListServlet.class);
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

		try {
			List<Order> orders = orderService.getAllOrders();

			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<meta charset=\"UTF-8\">");
			out.println("<title>Список заказов</title>");
			out.println("<style>");
			out.println("table { border-collapse: collapse; width: 100%; }");
			out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
			out.println("th { background-color: #f2f2f2; }");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Список заказов</h1>");
			out.println("<table>");
			out.println("<tr><th>ID</th><th>Дата</th><th>Клиент</th><th>Сумма</th><th>Статус</th><th>Адрес</th></tr>");

			for (Order order : orders) {
				out.println("<tr>");
				out.println("<td>" + order.getOrderId() + "</td>");
				out.println("<td>" + order.getOrderDate() + "</td>");
				out.println("<td>" + (order.getCustomer() != null ? order.getCustomer().getName() : "N/A") + "</td>");
				out.println("<td>" + order.getTotalPrice() + "</td>");
				out.println("<td>" + order.getStatus() + "</td>");
				out.println("<td>" + order.getShippingAddress() + "</td>");
				out.println("</tr>");
			}

			out.println("</table>");
			out.println("<br/>");
			out.println("<a href='" + req.getContextPath() + "/order/create'><button>Создать новый заказ</button></a>");
			out.println("</body>");
			out.println("</html>");
		} catch (Exception e) {
			logger.error("Ошибка при получении списка заказов", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при получении списка заказов");
		} finally {
			out.close();
		}
	}
}