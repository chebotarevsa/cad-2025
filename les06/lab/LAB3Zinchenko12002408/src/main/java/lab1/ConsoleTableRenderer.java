package lab1;

import lab1.model.Product;
import org.springframework.stereotype.Component;

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

        System.out.println("+----+----------------------+----------------------+----------+");
        System.out.println("| ID | Name                 | Description          | Price    |");
        System.out.println("+----+----------------------+----------------------+----------+");

        for (Product p : products) {
            System.out.printf("| %-2d | %-20s | %-20s | %-8s |%n",
                    p.getProductId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice());
        }

        System.out.println("+----+----------------------+----------------------+----------+");
    }
}