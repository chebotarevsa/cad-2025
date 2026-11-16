package ru.bsuedu.cad.lab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    private final DataSource dataSource;
    private final ConcreteCategoryProvider categoryProvider;

    public DataBaseRenderer(DataSource dataSource, ConcreteCategoryProvider categoryProvider) {
        this.dataSource = dataSource;
        this.categoryProvider = categoryProvider;
    }

    @Override
    public void render(List<Product> products) {
        try (Connection conn = dataSource.getConnection()) {

            String sqlCat = "INSERT INTO CATEGORIES(ID, NAME, DESCRIPTION) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCat)) {
                for (Category c : categoryProvider.getCategories()) {
                    ps.setLong(1, c.getId());
                    ps.setString(2, c.getName());
                    ps.setString(3, c.getDescription());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String sqlProd = "INSERT INTO PRODUCTS(ID, NAME, DESCRIPTION, PRICE, STOCK_QUANTITY, CATEGORY_ID) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlProd)) {
                for (Product p : products) {
                    ps.setLong(1, p.getId());
                    ps.setString(2, p.getName());
                    ps.setString(3, p.getDescription());
                    ps.setDouble(4, p.getPrice());
                    ps.setInt(5, p.getStockQuantity());
                    ps.setLong(6, p.getCategoryId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            System.out.println("Данные сохранены");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}