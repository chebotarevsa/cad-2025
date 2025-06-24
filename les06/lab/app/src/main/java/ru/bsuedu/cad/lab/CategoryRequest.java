package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);
    private final JdbcTemplate jdbcTemplate;

    public CategoryRequest(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void execute() {
        String sql = "SELECT c.name, COUNT(p.product_id) as product_count " +
                "FROM CATEGORIES c JOIN PRODUCTS p ON c.category_id = p.category_id " +
                "GROUP BY c.name HAVING COUNT(p.product_id) > 1";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> row : results) {
            logger.info("Категория: {}, количество продуктов: {}",
                    row.get("name"),
                    row.get("product_count"));
        }
    }
}