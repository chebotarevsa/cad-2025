package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.List;

@Component("dbRenderer")
public class DataBaseRenderer implements Renderer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void render(List<Product> products) {
        for (Product product : products) {
            jdbcTemplate.update(
                    "INSERT INTO PRODUCTS (name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    product.getName(),
                    "Description for " + product.getName(),
                    1,
                    product.getPrice(),
                    10,
                    "",
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()));
            System.out.println("Saved to DB: " + product.getName());
        }
    }
}