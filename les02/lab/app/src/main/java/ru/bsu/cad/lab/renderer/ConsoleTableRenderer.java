package ru.bsu.cad.lab.renderer;

import org.springframework.stereotype.Component;
import ru.bsu.cad.lab.model.Product;
import ru.bsu.cad.lab.provider.ProductProvider;

import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        
        System.out.println("+------------+----------------------+----------------------+------------+------------+------------+----------------------+----------------------+");
        System.out.println("| Product ID | Name                 | Description          | Category   | Price      | Quantity   | Created At           | Updated At           |");
        System.out.println("+------------+----------------------+----------------------+------------+------------+------------+----------------------+----------------------+");
        
        for (Product product : products) {
            System.out.printf("| %-10d | %-20s | %-20s | %-10d | %-10.2f | %-10d | %-20s | %-20s |\n",
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryId(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getCreatedAt(),
                    product.getUpdatedAt());
        }
        
        System.out.println("+------------+----------------------+----------------------+------------+------------+------------+----------------------+----------------------+");
    }
}
