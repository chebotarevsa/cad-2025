package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataBaseRenderer(ProductProvider productProvider,
                            CategoryProvider categoryProvider,
                            JdbcTemplate jdbcTemplate) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void render() {
        insertCategories();
        insertProducts();
        System.out.println("Информация успешно интегрирована в реляционную базу данных.");
    }

    private void insertCategories() {
        List<Category> categories = categoryProvider.getCategories();
        for (Category c : categories) {
            jdbcTemplate.update(
                "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)",
                c.getId(), c.getName(), c.getDescription()
            );
        }
    }

    private void insertProducts() {
        List<Product> products = productProvider.getProducts();
        java.sql.Timestamp now = java.sql.Timestamp.valueOf(LocalDateTime.now());
        for (Product p : products) {
            jdbcTemplate.update(
                "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                p.getProductId(),
                p.getName(),
                p.getDescription(),
                p.getCategoryId(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getImageUrl(),
                now,
                now
            );
        }
    }
}