package ru.bsu.cad.lab;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.bsu.cad.lab.config.AppConfig;
import ru.bsu.cad.lab.renderer.Renderer;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void contextLoads() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Renderer renderer = context.getBean(Renderer.class);
            assertNotNull(renderer, "Renderer bean should exist");
        }
    }
}