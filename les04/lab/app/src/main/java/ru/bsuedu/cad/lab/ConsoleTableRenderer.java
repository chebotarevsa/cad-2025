package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products) {
        System.out.println("+----------------------+-----------------+------------+");
        System.out.println("| Name                 | Category        | Price      |");
        System.out.println("+----------------------+-----------------+------------+");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println("+----------------------+-----------------+------------+");
    }
}