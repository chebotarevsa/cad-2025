package lab1.service;

import lab1.repository.ProductRepository;
import lab1.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseLogger {

    private final ProductRepository repo;

    public DatabaseLogger(ProductRepository repo) {
        this.repo = repo;
    }

    public void logProducts() {
        List<Product> products = repo.findAll();
        System.out.println("=== PRODUCTS FROM DATABASE ===");
        products.forEach(p ->
                System.out.println(
                        p.getProductId() + " | " +
                                p.getName() + " | " +
                                p.getDescription() + " | " +
                                p.getCategoryId() + " | " +
                                p.getPrice() + " | " +
                                p.getStockQuantity() + " | " +
                                p.getImageUrl() + " | " +
                                p.getCreatedAt() + " | " +
                                p.getUpdatedAt()
                )
        );
    }
}