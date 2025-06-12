package ru.bsu.cad.lab.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CategoryRequest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);
    private final JdbcTemplate jdbcTemplate;

    // Внедряем DataSource через конструктор
    public CategoryRequest(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void printCategoriesWithProducts() {
        // Сначала проверим, есть ли данные в таблицах
        Integer categoryCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM CATEGORIES", Integer.class);
        Integer productCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM PRODUCTS", Integer.class);
        
        logger.info("Categories in DB: {}", categoryCount);
        logger.info("Products in DB: {}", productCount);
    
        // Основной запрос
        String sql = """
            SELECT c.name AS category_name, COUNT(p.product_id) AS product_count
            FROM CATEGORIES c
            JOIN PRODUCTS p ON c.category_id = p.category_id
            WHERE p.stock_quantity > 1
            GROUP BY c.name
            """;
    
        logger.info("Categories with products (stock > 1):");
        jdbcTemplate.query(sql, (rs) -> {
            logger.info("{}: {} products", 
                rs.getString("category_name"), 
                rs.getInt("product_count"));
        });
    }
}