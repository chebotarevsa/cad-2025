package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        ProductProvider provider = context.getBean(ProductProvider.class);
        Renderer renderer = context.getBean(Renderer.class);
        
        var products = provider.getProducts();
        renderer.render(products);
    }
}