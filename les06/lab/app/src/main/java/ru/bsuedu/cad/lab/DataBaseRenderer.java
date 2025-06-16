package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Primary
@Component("databaseRenderer")
public class DataBaseRenderer implements Renderer{
    private JdbcTemplate jdbcTemplate;
    private Provider<Product> productProvider;
    private Provider<Category> categoryProvider;

    @Autowired    
    public DataBaseRenderer(JdbcTemplate jdbcTemplate, Provider<Product> productProvider, Provider<Category> categoryProvider){
        this.jdbcTemplate = jdbcTemplate;
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
    }

    @Override
    public void render() throws NumberFormatException, IOException, ParseException {

        for (Category c : categoryProvider.getEntitees()) {
            insertCategory(c);
        }

        System.out.println("Product DataBase:");
        for (Product p : productProvider.getEntitees()) {
            insertProduct(p);
            System.out.println(p);
        }

        System.out.println("\nRequest to DataBase:");
        for(Category c : categoryRequest())
        {
            System.out.println(c);
        }

        System.out.println("DB Renderer");
    }
        public void insertProduct(Product product) {
        String sql = "INSERT INTO PRODUCTS (id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at)" +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.id, product.name, product.description, product.category_id, product.price, 
        product.stock_quantity, product.image_url, product.created_at, product.updated_at);
    }

    public void insertCategory(Category category) {
        String sql = "INSERT INTO CATEGORIES (id, name, description)" +
        " VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, category.id, category.name, category.description);
    }

    public List<Category> categoryRequest()
    {
        String sql = "SELECT c.id, c.name, c.description " +
                     "FROM Categories c " +
                     "JOIN Products p ON p.category_id = c.id " +
                     "GROUP BY c.id, c.name, c.description " +
                     "HAVING COUNT(p.id) > 1";

        return jdbcTemplate.query(sql, categoryRowMapper());
    }

    private RowMapper<Category> categoryRowMapper() {
        return (rs, rowNum) -> new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"));
    }

}


// @Component("consoleRenderer")
// public class ConsoleTableRenderer implements Renderer {
//     private Provider<Product> provider;

//     @Autowired
//     public ConsoleTableRenderer(Provider<Product> provider) {
//         this.provider = provider;
//     }

//     public void render() throws NumberFormatException, IOException, ParseException{
//         List<Product> tabList = this.provider.getEntitees();

//         System.out.println("_____________________________________________________");
//         for(int i = 0; i < tabList.size(); i++){
//             System.out.println("|" + tabList.get(i).product_id + " | "
//                                    + tabList.get(i).name + " | "
//                                    + tabList.get(i).description + " | "
//                                    + tabList.get(i).category_id + " | "
//                                    + tabList.get(i).price + " | "
//                                    + tabList.get(i).stock_quantity + " | "
//                                    + tabList.get(i).image_url + " | "
//                                    + calendarToString(tabList.get(i).created_at) + " | "
//                                    + calendarToString(tabList.get(i).updated_at) + " | "
//             );
//             System.out.println("_____________________________________________________");
//         }
//     }

//     public String calendarToString(Calendar calendar){
//         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//         return sdf.format(calendar.getTime());
//     }
// }
