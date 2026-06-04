package ru.bsuedu.cad.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.bsuedu.cad.lab.JpaConfig;

public class App {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(JpaConfig.class);
        context.getBean(Client.class).run();
        context.close();
    }
}
