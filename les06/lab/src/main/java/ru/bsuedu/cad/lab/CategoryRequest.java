package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CategoryRequest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void logCategoriesWithMoreThanOneProduct() {
        String sql = "SELECT c.name, COUNT(p.product_id) as product_count " +
                     "FROM categories c " +
                     "JOIN products p ON c.category_id = p.category_id " +
                     "GROUP BY c.category_id " +
                     "HAVING COUNT(p.product_id) > 1";

        jdbcTemplate.query(sql, rs -> {
            String name = rs.getString("name");
            int count = rs.getInt("product_count");
            logger.info("Category: {} - Product count: {}", name, count);
        });
    }
}
