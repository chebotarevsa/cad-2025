package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("ru.bsuedu.cad.lab")
public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class)) {
            ProductProvider provider = context.getBean(ProductProvider.class);
            Renderer renderer = context.getBean("htmlRenderer", Renderer.class);
            renderer.render(provider.getProducts());
        }
    }
}