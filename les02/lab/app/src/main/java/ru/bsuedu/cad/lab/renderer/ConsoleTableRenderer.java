package ru.bsuedu.cad.lab.renderer;

import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.util.List;

public class ConsoleTableRenderer implements Renderer {

    private final ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        System.out.printf("%-3s | %-35s | %-10s | %-7s | %-8s\n", "ID", "Название", "Цена", "Остаток", "Категория");
        System.out.println("----+-------------------------------------+------------+---------+----------");

        for (Product p : products) {
            System.out.printf(
                "%-3d | %-35s | %-10s | %-7d | %-8d\n",
                p.getProductId(),
                p.getName(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getCategoryId()
            );
        }
    }
}
