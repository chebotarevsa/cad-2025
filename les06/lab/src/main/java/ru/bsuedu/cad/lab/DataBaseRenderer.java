package ru.bsuedu.cad.lab;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataBaseRenderer implements Renderer {

    private final JdbcTemplate jdbcTemplate;
    private final ConcreteCategoryProvider categoryProvider;
    private final ProductProvider productProvider;

    @Autowired
    public DataBaseRenderer(DataSource dataSource,
                            ConcreteCategoryProvider categoryProvider,
                            ProductProvider productProvider) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.categoryProvider = categoryProvider;
        this.productProvider = productProvider;
    }

    @Override
    public void render(List<Product> products) {
        // Очистка таблиц
        jdbcTemplate.execute("DELETE FROM PRODUCTS");
        jdbcTemplate.execute("DELETE FROM CATEGORIES");

        // Вставка категорий
        List<Category> categories = categoryProvider.loadCategories();
        for (Category cat : categories) {
            jdbcTemplate.update("INSERT INTO CATEGORIES (id, name) VALUES (?, ?)", cat.getId(), cat.getName());
        }

        // Вставка продуктов (временно category_id = 1)
        for (Product p : products) {
            jdbcTemplate.update("INSERT INTO PRODUCTS (id, name, price, category_id) VALUES (?, ?, ?, ?)",
                    p.getId(), p.getName(), p.getPrice(), 1);
        }
        System.out.println("Данные сохранены в БД.");
    }
}