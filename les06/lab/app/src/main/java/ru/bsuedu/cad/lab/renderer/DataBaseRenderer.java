package ru.bsuedu.cad.lab.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.CategoryProvider;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.util.List;

@Component
@Primary
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
        saveCategories(categoryProvider.getCategories());
        saveProducts(productProvider.getProducts());
        System.out.println("Данные успешно сохранены в базу данных H2.");
    }

    private void saveCategories(List<Category> categories) {
        String sql = "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)";
        for (Category c : categories) {
            jdbcTemplate.update(sql, c.getCategoryId(), c.getName(), c.getDescription());
        }
    }

    private void saveProducts(List<Product> products) {
        String sql = "INSERT INTO PRODUCTS " +
                "(product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (Product p : products) {
            jdbcTemplate.update(sql,
                    p.getProductId(),
                    p.getName(),
                    p.getDescription(),
                    p.getCategoryId(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    p.getImageUrl(),
                    p.getCreatedAt(),
                    p.getUpdatedAt());
        }
    }
}
