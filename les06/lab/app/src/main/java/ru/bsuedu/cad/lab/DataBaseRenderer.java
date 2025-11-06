package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component("dbRenderer")
public class DataBaseRenderer implements Renderer{
    private Provider <Product> productProvider;
    private Provider <Category> categoryProvider;
    final private JdbcTemplate jdbcTemplate;

    public DataBaseRenderer(Provider <Product> productProvider, Provider <Category> categoryProvider, JdbcTemplate jdbcTemplate){
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void render() {
        // productProvider.getProducts();
        // categoryProvider.getCategory();
        for(Product p : productProvider.getItems(Product.class, 0))
        {
            insertProduct(p);
        }
        for(Category c : categoryProvider.getItems(Category.class, 1))
        {
            insertCategory(c);
        }
        categoryRequest();
        
        System.out.println("DB");
    }
    
    public void insertProduct(Product product) {
        // String sql = "INSERT INTO table (PRODUCTS) VALUES (id, description, category_id, price DECFLOAT(20), stock_quantity, image_url, created_at, updated_at)";
        String sql = "INSERT INTO PRODUCTS (id, name, description, price, stock_quantity, image_url, created_at, updated_at)" +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.product_id, product.name, product.description, product.category_id, product.price, product.stock_quantity, product.image_url, product.created_at, product.updated_at);
    }
    public void insertCategory(Category category) {
        // String sql = "INSERT INTO table (PRODUCTS) VALUES (id, description, category_id, price DECFLOAT(20), stock_quantity, image_url, created_at, updated_at)";
        String sql = "INSERT INTO CATEGORIES (id, name, description)" +
        " VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, category.product_id, category.name, category.description);
    }

    public List<Category> categoryRequest(){
        String sql = "SELECT * FROM CATEGORIES";
        return jdbcTemplate.query(sql, categoryRowMapper());
    }
    private RowMapper<Category> categoryRowMapper() {
        return (rs, rowNum) -> new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"));
    }
}
