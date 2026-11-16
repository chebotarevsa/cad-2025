package ru.bsuedu.cad.lab;

import java.util.List;

public class ConsoleTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("| %-3s | %-40s | %-10s | %-10s |%n",
                "ID", "Название", "Цена", "Остаток");
        System.out.println("----------------------------------------------------------------------------");

        for (Product p : products) {
            System.out.printf("| %-3d | %-40s | %10.2f | %10d |%n",
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStockQuantity()
            );
        }
        System.out.println("----------------------------------------------------------------------------");
    }
}
