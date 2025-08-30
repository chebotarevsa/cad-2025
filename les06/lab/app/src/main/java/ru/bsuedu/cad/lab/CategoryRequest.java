package ru.bsuedu.cad.lab;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest implements Request<Category> {
	private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public CategoryRequest(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Category> execute() {
		try {
			String sql = "SELECT DISTINCT c.* FROM categories c " +
					"WHERE EXISTS (SELECT 1 FROM products p " +
					"WHERE p.category_id = c.category_id " +
					"GROUP BY p.category_id HAVING COUNT(*) > 1)";

			List<Category> result = jdbcTemplate.query(sql, (rs, rowNum) -> new Category(
					rs.getInt("category_id"),
					rs.getString("name"),
					rs.getString("description")));

			logger.info("Found {} categories with multiple products", result.size());
			return result;
		} catch (Exception e) {
			logger.error("Error executing request", e);
			throw e;
		}
	}
}
