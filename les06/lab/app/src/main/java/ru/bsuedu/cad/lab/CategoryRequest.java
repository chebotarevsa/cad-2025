package ru.bsuedu.cad.lab;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void logCategoriesWithMultipleProducts() {
        String sql = """
            SELECT c.category_id, c.name, c.description, COUNT(p.product_id) as product_count
            FROM CATEGORIES c
            JOIN PRODUCTS p ON c.category_id = p.category_id
            GROUP BY c.category_id, c.name, c.description
            HAVING COUNT(p.product_id) > 1
            ORDER BY product_count DESC
            """;

        RowMapper<Category> rowMapper = (rs, rowNum) -> {
            int categoryId = rs.getInt("category_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            return new Category(categoryId, name, description);
        };

        List<Category> categories = jdbcTemplate.query(sql, rowMapper);

        for (Category category : categories) {
            logger.info("Категория: {} (ID: {}), {}", category.getName(), category.getCategoryId(), category.getDescription());
        }
    }
}