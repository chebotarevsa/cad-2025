package ru.bsuedu.cad.lab.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading data from CSV");
        insertCategories();
        insertCustomers();
        insertProducts();
    }
    private void insertCategories() throws Exception {
        String sql = "INSERT INTO categories (category_id, name, description) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            List<String[]> rows = readCsv("category.csv");

            for (String[] row : rows) {
                ps.setInt(1, Integer.parseInt(row[0]));
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        }
    }

    private void insertCustomers() throws Exception {
        String sql = "INSERT INTO customers (customer_id, name, email, phone, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            List<String[]> rows = readCsv("customer.csv");

            for (String[] row : rows) {
                ps.setInt(1, Integer.parseInt(row[0]));
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.setString(4, row[3]);
                ps.setString(5, row[4]);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        }
    }

    private void insertProducts() throws Exception {
        String sql = """
                INSERT INTO products
                (product_id, name, description, category_id, price,
                 stock_quantity, image_url, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            List<String[]> rows = readCsv("product.csv");

            for (String[] row : rows) {
                String[] firstDate = row[7].split("-");
                String[] secondDate = row[8].split("-");
                ps.setInt(1, Integer.parseInt(row[0]));
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.setInt(4, Integer.parseInt(row[3]));
                ps.setBigDecimal(5, new BigDecimal(row[4]));
                ps.setInt(6, Integer.parseInt(row[5]));
                ps.setString(7, row[6]);
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.of(LocalDate.of(Integer.parseInt(firstDate[0]), Integer.parseInt(firstDate[1]), Integer.parseInt(firstDate[2])), LocalTime.of(0,0,0))));
                ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.of(LocalDate.of(Integer.parseInt(secondDate[0]), Integer.parseInt(secondDate[1]), Integer.parseInt(secondDate[2])), LocalTime.of(0,0,0))));

                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        }
    }

    private List<String[]> readCsv(String filename) throws Exception {
        List<String[]> list = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(filename).getInputStream())
        );

        // skip header
        reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            list.add(line.split(","));
        }

        return list;
    }
}