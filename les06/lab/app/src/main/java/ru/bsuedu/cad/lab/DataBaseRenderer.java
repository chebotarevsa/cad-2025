package ru.bsuedu.cad.lab;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("dataBaseRenderer")
public class DataBaseRenderer implements Renderer {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseRenderer.class);

    private final JdbcTemplate jdbcTemplate;
    private DataProvider<Product> productProvider;
    private DataProvider<Category> categoryProvider;
    private Request<Category> categoryRequest;

    @Autowired
    public DataBaseRenderer(JdbcTemplate jdbcTemplate, 
                          DataProvider<Product> productProvider,
                          DataProvider<Category> categoryProvider,
                          Request<Category> categoryRequest) {
        this.jdbcTemplate = jdbcTemplate;
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.categoryRequest = categoryRequest;
    }

    @Override
    public void render() {
        // Insert categories
        for (Category c : categoryProvider.getItems()) {
            insertCategory(c);
        }
        
        // Insert products
        for (Product p : productProvider.getItems()) {
            insertProduct(p);
        }
        
        // Execute query and log results
        try {
            logger.info("Starting database rendering");
            
            List<Category> categories = categoryRequest.execute();
            categories.forEach(c -> logger.info("Category with multiple products: {}", c));
            
            logger.info("Database rendering completed successfully");
        } catch (Exception e) {
            logger.error("Database rendering failed", e);
        }
    }

    private void insertCategory(Category category) {
        String sql = "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, category.category_id, category.name, category.description);
    }

    private void insertProduct(Product product) {
        String sql = "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, " +
                    "stock_quantity, image_url, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            product.product_id, 
            product.name, 
            product.description,
            product.category_id,
            product.price,
            product.stock_quantity,
            product.image_url,
            product.created_at,
            product.updated_at);
    }
}