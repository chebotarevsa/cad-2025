package ru.bsuedu.cad.lab;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
@RequiredArgsConstructor
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final EmbeddedDatabase embeddedDatabase;

    @Override
    public void render() {
        try (Connection connection = embeddedDatabase.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)")) {

                categoryProvider.getCategories().forEach(category -> {
                    try {
                        statement.setInt(1, category.getCategoryId());
                        statement.setString(2, category.getName());
                        statement.setString(3, category.getDescription());
                        statement.addBatch();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                statement.executeBatch();
            }

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO PRODUCTS\n" +
                    "                (product_id, name, description, category_id, price,\n" +
                    "                 stock_quantity, image_url, created_at, updated_at)\n" +
                    "                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                productProvider.getProducts().forEach(product -> {
                    try {
                        statement.setLong(1, product.getProductId());
                        statement.setString(2, product.getName());
                        statement.setString(3, product.getDescription());
                        statement.setInt(4, product.getCategoryId());
                        statement.setBigDecimal(5, product.getPrice());
                        statement.setInt(6, product.getStockQuantity());
                        statement.setString(7, product.getImageUrl());
                        statement.setTimestamp(8, new Timestamp(product.getCreatedAt().getTime()));
                        statement.setTimestamp(9, new Timestamp(product.getUpdatedAt().getTime()));
                        statement.addBatch();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                statement.executeBatch();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
