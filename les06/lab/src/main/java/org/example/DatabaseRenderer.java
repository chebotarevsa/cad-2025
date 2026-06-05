package org.example;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@Primary
public class DatabaseRenderer implements Renderer {

    private final JdbcTemplate jdbcTemplate;
    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final CategoryRequest categoryRequest;

    public DatabaseRenderer(DataSource dataSource,
                            ProductProvider productProvider,
                            CategoryProvider categoryProvider,
                            CategoryRequest categoryRequest) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.categoryRequest = categoryRequest;
    }

    @Override
    public void render() {
        jdbcTemplate.execute("DELETE FROM PRODUCTS");
        jdbcTemplate.execute("DELETE FROM CATEGORIES");

        List<Category> categories = categoryProvider.getCategories();
        for (Category category : categories) {
            jdbcTemplate.update(
                    "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)",
                    category.getCategoryId(),
                    category.getName(),
                    category.getDescription()
            );
        }
        System.out.println("Categories saved: " + categories.size());

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
        System.out.println("Products saved: " + products.size());

        categoryRequest.printCategoriesWithMoreThanOneProduct();
    }
}