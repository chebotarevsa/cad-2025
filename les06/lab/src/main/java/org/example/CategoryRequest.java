package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;

    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void printCategoriesWithMoreThanOneProduct() {
        String sql = "SELECT c.category_id, c.name, COUNT(p.product_id) AS product_count " +
                "FROM CATEGORIES c " +
                "JOIN PRODUCTS p ON c.category_id = p.category_id " +
                "GROUP BY c.category_id, c.name " +
                "HAVING COUNT(p.product_id) > 1";

        List<String> rows = jdbcTemplate.query(sql, (rs, rowNum) ->
                "Category: " + rs.getString("name")
                        + ", products: " + rs.getInt("product_count")
        );

        logger.info("Categories with more than one product:");

        for (String row : rows) {
            logger.info(row);
        }
    }
}