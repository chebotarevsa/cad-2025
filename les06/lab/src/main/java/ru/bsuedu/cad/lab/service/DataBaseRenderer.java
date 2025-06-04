
package ru.bsuedu.cad.lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.Renderer;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

@Component
public class DataBaseRenderer implements Renderer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConcreteCategoryProvider categoryProvider;

    @Autowired
    private ConcreteProductProvider productProvider;

    @Override
    public void render() {
        List<Category> categories = categoryProvider.loadCategories();
        List<Product> products = productProvider.loadProducts();

        for (Category category : categories) {
            jdbcTemplate.update("INSERT INTO CATEGORIES (ID, NAME) VALUES (?, ?)", category.getId(), category.getName());
        }

        for (Product product : products) {
            jdbcTemplate.update("INSERT INTO PRODUCTS (ID, NAME, DESCRIPTION, CATEGORY_ID, PRICE, STOCK_QUANTITY, IMAGE_URL, CREATED_AT, UPDATED_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    product.getProductId(), product.getName(), product.getDescription(), product.getCategoryId(),
                    product.getPrice(), product.getStockQuantity(), product.getImageUrl(), product.getCreatedAt(), product.getUpdatedAt());
        }
    }
}
