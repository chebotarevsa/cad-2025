package lab1;

import lab1.model.Category;
import lab1.model.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    private final ConcreteProductProvider productProvider;
    private final ConcreteCategoryProvider categoryProvider;
    private final JdbcTemplate jdbc;

    public DataBaseRenderer(ConcreteProductProvider productProvider,
                            ConcreteCategoryProvider categoryProvider,
                            JdbcTemplate jdbc) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbc = jdbc;
    }

    @Override
    public void render() {

        List<Category> categories = categoryProvider.getCategories();
        List<Product> products = productProvider.getProducts();

        jdbc.update("DELETE FROM PRODUCTS");
        jdbc.update("DELETE FROM CATEGORIES");

        for (Category c : categories) {
            jdbc.update("INSERT INTO CATEGORIES VALUES (?, ?, ?)",
                    c.getCategoryId(), c.getName(), c.getDescription());
        }

        for (Product p : products) {
            jdbc.update("INSERT INTO PRODUCTS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    p.getProductId(),
                    p.getName(),
                    p.getDescription(),
                    p.getCategoryId(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    p.getImageUrl(),
                    p.getCreatedAt(),
                    p.getUpdatedAt()
            );
        }

        System.out.println("Данные из CSV успешно сохранены в базу данных.");
    }
}