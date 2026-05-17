package ru.bsuedu.cad.lab;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    @Autowired
    private ProductProvider productProvider;

    @Autowired
    private CategoryProvider categoryProvider;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void render() throws Exception {
        List<Category> categories = categoryProvider.getCategories();
        for (Category category : categories) {
            jdbcTemplate.update(
                "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)",
                category.getCategoryId(), category.getName(), category.getDescription()
            );
        }

        List<Product> products = productProvider.getProducts();
        for (Product product : products) {
            jdbcTemplate.update(
                "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                product.getProductId(), product.getName(), product.getDescription(),
                product.getCategoryId(), product.getPrice(), product.getStockQuantity(),
                product.getImageUrl(), product.getCreatedAt(), product.getUpdatedAt()
            );
        }
    }
}