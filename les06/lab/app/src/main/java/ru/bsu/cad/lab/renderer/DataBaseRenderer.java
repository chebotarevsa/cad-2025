package ru.bsu.cad.lab.renderer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;
import ru.bsu.cad.lab.model.Product;
import ru.bsu.cad.lab.model.Category;
import ru.bsu.cad.lab.provider.ProductProvider;
import ru.bsu.cad.lab.provider.CategoryProvider;

import javax.sql.DataSource;
import java.util.List;

@Component
@Primary // Помечаем как реализацию Renderer по умолчанию
public class DataBaseRenderer implements Renderer {
    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;

    // Конструктор с зависимостями
    public DataBaseRenderer(ProductProvider productProvider,
                          CategoryProvider categoryProvider,
                          DataSource dataSource) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void render() {
        System.out.println("=== Начало сохранения в БД ===");
        
        List<Category> categories = categoryProvider.getCategories();
        List<Product> products = productProvider.getProducts();
    
        System.out.println("Categories to insert: " + categories.size());
        System.out.println("Products to insert: " + products.size());
    
        // Insert categories
        categories.forEach(category -> {
            System.out.println("Inserting category: " + category.getName());
            jdbcTemplate.update(
                "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)",
                category.getCategoryId(),
                category.getName(),
                category.getDescription()
            );
        });
    
        // Insert products
        products.forEach(product -> {
            System.out.println("Inserting product: " + product.getName());
            jdbcTemplate.update(
                "INSERT INTO PRODUCTS (product_id, name, description, category_id, " +
                "price, stock_quantity, image_url, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
        });
        
        System.out.println("=== Данные успешно сохранены в БД ===");
    }
}