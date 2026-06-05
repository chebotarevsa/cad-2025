package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;

public class App {
    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println("Application context started.");
            // DataLoader is a component that will run on startup and create sample data
            Thread.sleep(500); // brief pause to let DataLoader finish logs
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Application finished.");
    }
}
