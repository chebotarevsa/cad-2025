package ru.bsuedu.cad.lab;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CategoryRequest {

    private final EmbeddedDatabase dataSource;

    public void getCategoriesWithMoreThanOneProduct() {

        String sql = """
                SELECT c.category_id, c.name, c.description
                FROM CATEGORIES c
                JOIN PRODUCTS p ON c.category_id = p.category_id
                GROUP BY c.category_id, c.name, c.description
                HAVING COUNT(p.product_id) > 1
                """;

        List<Category> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        log.info("Categories with more than one product found {}", result);
    }
}
