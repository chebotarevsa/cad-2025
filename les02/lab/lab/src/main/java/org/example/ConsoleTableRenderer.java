package org.example;
import java.util.List;

public class ConsoleTableRenderer implements Renderer {

    private ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        System.out.println("+----+------------------------------------------+------------+---------+--------+");
        System.out.println("| ID | Название                                 | Категория  | Цена    | Кол-во |");
        System.out.println("+----+------------------------------------------+------------+---------+--------+");

        for (Product p : products) {
            System.out.printf(
                    "| %-2d | %-40s | %-10d | %-8s | %-5d |\n",
                    p.getProductId(),
                    p.getName(),
                    p.getCategoryId(),
                    p.getPrice(),
                    p.getStockQuantity()
            );
        }

        System.out.println("+----+------------------------------------------+------------+----------+-------+");
    }

}