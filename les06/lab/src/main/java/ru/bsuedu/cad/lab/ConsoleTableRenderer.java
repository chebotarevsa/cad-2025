package ru.bsuedu.cad.lab;

import java.util.List;

// @Component  
public class ConsoleTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {
        System.out.println("+------+--------------------------------+----------+-----------------+");
        System.out.printf("| %-4s | %-30s | %-8s | %-15s |\n", "ID", "Name", "Price", "Category");
        System.out.println("+------+--------------------------------+----------+-----------------+");
        for (Product p : products) {
            System.out.printf("| %-4d | %-30s | %-8.2f | %-15s |\n", 
                p.getId(), p.getName(), p.getPrice(), p.getCategory());
        }
        System.out.println("+------+--------------------------------+----------+-----------------+");
    }
}