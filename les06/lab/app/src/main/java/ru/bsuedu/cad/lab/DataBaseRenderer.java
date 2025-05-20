package ru.bsuedu.cad.lab;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component("DataBaseRenderer")
public class DataBaseRenderer implements Renderer{

    final private Provider<Category> categoryProvider;
    final private Provider<Product> productProvider;
    private static Logger LOGGER = LoggerFactory.getLogger(DataBaseRenderer.class);
    final private JdbcTemplate jdbcTemplate;

    public DataBaseRenderer(Provider<Category> categoryProvider, Provider<Product> productProvider, JdbcTemplate jdbcTemplate) {
        this.categoryProvider = categoryProvider;
        this.productProvider = productProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void render() {
        for (var c : categoryProvider.getItems()) {
            addCategory(c);
        }
        for (var p : productProvider.getItems()) {
            addProduct(p);
        }
        for (var c  : categoryRequest()) {
            //LOGGER.info(String.valueOf(c.getCategoryID()));
            LOGGER.info(c.getName());
        }

    }


    public void addCategory(Category category) {
        String sql = "INSERT INTO Categories (category_id, name, description)"
                +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, category.getCategoryID(), category.getName(), category.getDescription());
    }
    public void addProduct(Product product) {
        String sql = "INSERT INTO Products (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at)"
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getProductID(), product.getName(), product.getDescription(), product.getCategoryID(),
        product.getPrice(), product.getStockQuantity(), product.getImageURL(), product.getCreatedAt(), product.getUpdatedAt());
    }

    public List<Category> categoryRequest(){
        String sql = "SELECT * FROM Categories WHERE category_id IN (SELECT Products.category_id FROM Categories JOIN Products on Categories.category_id = Products.category_id GROUP BY Products.category_id HAVING COUNT(Products.category_id) > 1)";
        // String sql = "SELECT * FROM Categories WHERE category_id IN (SELECT category_id FROM Products WHERE stock_quantity > 1)";
        return jdbcTemplate.query(sql, categoryRowMapper());
    }

    private RowMapper<Category> categoryRowMapper() {
        return (rs, rowNum) -> new Category(
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description"));
    
    }
}
