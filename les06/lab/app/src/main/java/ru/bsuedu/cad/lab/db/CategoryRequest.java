package ru.bsuedu.cad.lab.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void logCategoriesWithMultipleProducts() {
        String sql = """
            SELECT c.name, COUNT(p.product_id) as product_count
            FROM CATEGORIES c
            JOIN PRODUCTS p ON c.category_id = p.category_id
            GROUP BY c.category_id, c.name
            HAVING COUNT(p.product_id) > 1
            """;

        List<String> results = jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getString("name") + " (товаров: " + rs.getInt("product_count") + ")"
        );

        logger.info("Категории с более чем одним товаром:");
        results.forEach(name -> logger.info("→ {}", name));
    }
}
