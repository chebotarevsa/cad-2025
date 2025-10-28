package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class CategoryRequest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void executeQuery() {
        String sql = "SELECT c.name, COUNT(p.product_id) as product_count FROM CATEGORIES c LEFT JOIN PRODUCTS p ON c.category_id = p.category_id GROUP BY c.category_id, c.name HAVING COUNT(p.product_id) > 1";

        List<Object[]> results = jdbcTemplate.query(sql, new RowMapper<Object[]>() {
            @Override
            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Object[] { rs.getString("name"), rs.getInt("product_count") };
            }
        });

        for (Object[] row : results) {
            logger.info("Category: {}, Products count: {}", row[0], row[1]);
        }
    }
}