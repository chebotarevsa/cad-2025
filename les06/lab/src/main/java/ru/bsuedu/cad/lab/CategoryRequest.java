package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CategoryRequest {
    private static final Logger log = LoggerFactory.getLogger(CategoryRequest.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void execute() {
        String sql = "SELECT c.name, COUNT(p.id) as cnt " +
                     "FROM CATEGORIES c JOIN PRODUCTS p ON c.id = p.category_id " +
                     "GROUP BY c.name HAVING COUNT(p.id) > 1";
        List<String> result = jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getString("name") + " - " + rs.getInt("cnt") + " товаров");
        log.info("Категории с количеством товаров > 1:");
        result.forEach(log::info);
    }
}