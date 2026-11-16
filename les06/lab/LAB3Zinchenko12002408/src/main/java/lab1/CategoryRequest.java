package lab1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest {

    private static final Logger log = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbc;

    public CategoryRequest(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void printCategoriesWithMoreThanOneProduct() {

        jdbc.query(
                "SELECT c.name, COUNT(p.product_id) AS cnt " +
                        "FROM CATEGORIES c " +
                        "JOIN PRODUCTS p ON c.category_id = p.category_id " +
                        "GROUP BY c.name " +
                        "HAVING COUNT(p.product_id) > 1",
                (rs) -> {
                    log.info("Категория: {} — товаров: {}", rs.getString("name"), rs.getInt("cnt"));
                }
        );
    }
}