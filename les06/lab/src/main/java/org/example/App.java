package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            System.setOut(new java.io.PrintStream(System.out));
        }

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        Renderer renderer = ctx.getBean(Renderer.class);
        renderer.render();

        ctx.close();
    }
}