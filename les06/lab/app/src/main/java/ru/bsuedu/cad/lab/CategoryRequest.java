package ru.bsuedu.cad.lab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);
    private final DataSource dataSource;

    public CategoryRequest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute() {
        String sql = """
            SELECT c.NAME, COUNT(p.ID) AS CNT
            FROM CATEGORIES c
            JOIN PRODUCTS p ON c.ID = p.CATEGORY_ID
            GROUP BY c.NAME
            HAVING COUNT(p.ID) > 1
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                logger.info("Категория: {} | Количество товаров: {}", rs.getString("NAME"), rs.getInt("CNT"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка запроса категорий", e);
        }
    }
}