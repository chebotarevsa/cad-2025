package ru.bsuedu.cap.component;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        Renderer renderer = ctx.getBean("renderer", Renderer.class);
        renderer.render();
    }
}