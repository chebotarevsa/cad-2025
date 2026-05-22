package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CategoryRequest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;

    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void printCategoriesWithMoreThanOneProduct() {
        String sql = """
                SELECT c.category_id, c.name, COUNT(p.product_id) AS product_count
                FROM CATEGORIES c
                JOIN PRODUCTS p ON c.category_id = p.category_id
                GROUP BY c.category_id, c.name
                HAVING COUNT(p.product_id) > 1
                ORDER BY c.category_id
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        logger.info("Категории, в которых больше одного товара:");
        for (Map<String, Object> row : rows) {
            logger.info("{} - {} товара",
                    row.get("NAME"),
                    row.get("PRODUCT_COUNT"));
        }
    }
}
