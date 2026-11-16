package ru.bsuedu.cad.lab;

import java.util.List;

public class ConsoleTableRenderer implements Renderer {
    
    @Override
    public void render(List<Product> products) {
        String format = "| %-4s | %-30s | %-8s | %-15s |%n";
        
        System.out.println("+" + "-".repeat(6) + "+" + "-".repeat(32) + "+" + "-".repeat(10) + "+" + "-".repeat(17) + "+");
        System.out.printf(format, "ID", "Name", "Price", "Category");
        System.out.println("+" + "-".repeat(6) + "+" + "-".repeat(32) + "+" + "-".repeat(10) + "+" + "-".repeat(17) + "+");
        
        for (Product p : products) {
            System.out.printf(format, p.getId(), p.getName(), p.getPrice(), p.getCategory());
        }
        
        System.out.println("+" + "-".repeat(6) + "+" + "-".repeat(32) + "+" + "-".repeat(10) + "+" + "-".repeat(17) + "+");
    }
}