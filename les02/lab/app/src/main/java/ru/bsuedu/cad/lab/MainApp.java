package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Renderer renderer = context.getBean(Renderer.class);
            renderer.render();
        }
    }
}
