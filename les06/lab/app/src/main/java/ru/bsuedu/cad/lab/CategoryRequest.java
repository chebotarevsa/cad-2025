package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute() {
        String sql = "SELECT c.category_id, c.name, COUNT(p.product_id) AS product_count " +
                     "FROM CATEGORIES c " +
                     "JOIN PRODUCTS p ON c.category_id = p.category_id " +
                     "GROUP BY c.category_id " +
                     "HAVING COUNT(p.product_id) > 1";

        List<CategoryWithProductCount> result = jdbcTemplate.query(sql, new RowMapper<CategoryWithProductCount>() {
            @Override
            public CategoryWithProductCount mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                CategoryWithProductCount category = new CategoryWithProductCount();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setProductCount(rs.getInt("product_count"));
                return category;
            }
        });

        if (result.isEmpty()) {
            logger.info("Нет категорий с количеством товаров больше единицы.");
        } else {
            logger.info("Категории с количеством товаров больше единицы:");
            result.forEach(category -> logger.info("Категория: {}, Количество товаров: {}", category.getName(), category.getProductCount()));
        }
    }

    public static class CategoryWithProductCount {
        private int categoryId;
        private String name;
        private int productCount;

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }
    }
}
