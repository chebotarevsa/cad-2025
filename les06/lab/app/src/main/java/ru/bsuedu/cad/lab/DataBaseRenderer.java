package ru.bsuedu.cad.lab;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("renderer")
public class DataBaseRenderer implements Renderer {
    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRequest categoryRequest;

    public DataBaseRenderer(ProductProvider productProvider, CategoryProvider categoryProvider,
                            JdbcTemplate jdbcTemplate, CategoryRequest categoryRequest) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRequest = categoryRequest;
    }

    @Override
    public void render() {
        saveCategories();
        saveProducts();
        categoryRequest.printCategoriesWithMoreThanOneProduct();
    }

    private void saveCategories() {
        String sql = "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)";

        for (Category category : categoryProvider.getCategories()) {
            jdbcTemplate.update(sql, category.categoryId, category.name, category.description);
        }

        System.out.println("Категории сохранены в базу данных");
    }

    private void saveProducts() {
        String sql = """
                INSERT INTO PRODUCTS
                (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        for (Product product : productProvider.getProducts()) {
            jdbcTemplate.update(sql,
                    product.productId,
                    product.name,
                    product.description,
                    product.categoryId,
                    product.price,
                    product.stockQuantity,
                    product.imageUrl,
                    product.createdAt,
                    product.updatedAt);
        }

        System.out.println("Товары сохранены в базу данных");
    }
}
