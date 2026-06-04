package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;

    public DataBaseRenderer(ProductProvider productProvider,
                            CategoryProvider categoryProvider,
                            JdbcTemplate jdbcTemplate) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void render() {
        List<Category> categories = categoryProvider.getCategories();
        for (Category c : categories) {
            jdbcTemplate.update(
                "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)",
                c.categoryId, c.name, c.description
            );
        }
        System.out.println("Категории сохранены в БД: " + categories.size() + " записей");

        List<Product> products = productProvider.getProducts();
        for (Product p : products) {
            jdbcTemplate.update(
                "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, " +
                "stock_quantity, image_url, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?)",
                p.productId, p.name, p.description, p.categoryId, p.price,
                p.stockQuantity, p.imageUrl,
                new java.sql.Date(p.createdAt.getTime()),
                new java.sql.Date(p.updatedAt.getTime())
            );
        }
        System.out.println("Товары сохранены в БД: " + products.size() + " записей");
    }
}
