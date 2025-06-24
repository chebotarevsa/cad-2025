package ru.bsuedu.cad.lab;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.List;

@Component
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;

    public DataBaseRenderer(ProductProvider productProvider,
                            CategoryProvider categoryProvider,
                            DataSource dataSource) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void render() {
        List<Category> categories = categoryProvider.getCategories();
        for (Category category : categories) {
            jdbcTemplate.update(
                    "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)",
                    category.getCategoryId(),
                    category.getName(),
                    category.getDescription()
            );
        }
        List<Product> products = productProvider.getProducts();
        for (Product product : products) {
            jdbcTemplate.update(
                    "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, " +
                            "stock_quantity, image_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryId(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getImageUrl(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            );
        }
    }
}