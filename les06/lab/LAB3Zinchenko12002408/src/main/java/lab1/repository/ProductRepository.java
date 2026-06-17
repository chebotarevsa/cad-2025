package lab1.repository;

import lab1.mapper.ProductRowMapper;
import lab1.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbc;

    public ProductRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Product> findAll() {
        return jdbc.query("SELECT * FROM PRODUCTS", new ProductRowMapper());
    }

    public Product findById(int id) {
        return jdbc.queryForObject(
                "SELECT * FROM PRODUCTS WHERE product_id = ?",
                new ProductRowMapper(),
                id
        );
    }
}
