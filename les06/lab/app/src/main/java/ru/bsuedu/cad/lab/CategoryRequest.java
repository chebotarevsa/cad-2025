package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;

    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void query() {
        List<String> result = jdbcTemplate.query(
            "SELECT c.name FROM CATEGORIES c " +
            "JOIN PRODUCTS p ON c.category_id = p.category_id " +
            "GROUP BY c.category_id, c.name " +
            "HAVING COUNT(p.product_id) > 1",
            (rs, rowNum) -> rs.getString("name")
        );
        LOGGER.info("Категории с более чем одним товаром: {}", result);
    }
}
