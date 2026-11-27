package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
       ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        ProductProvider productProvider = context.getBean(ProductProvider.class);
        Renderer renderer = context.getBean(Renderer.class);
        
        renderer.render(productProvider.getProducts());
    }
}