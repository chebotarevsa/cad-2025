package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class App {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductProvider provider = ctx.getBean(ProductProvider.class);
        List<Product> products = provider.getProducts();

        System.out.println("Загружено продуктов: " + products.size());

        for (int i = 0; i < Math.min(5, products.size()); i++) {
            Product p = products.get(i);
            System.out.println(p.getProductId() + " | " + p.getName() + " | " + p.getPrice());
        }

        Renderer renderer = ctx.getBean(Renderer.class);
        renderer.render();

        ctx.close();
    }
}
