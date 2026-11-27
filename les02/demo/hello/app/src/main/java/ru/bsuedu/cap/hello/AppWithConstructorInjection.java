package ru.bsuedu.cap.hello;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppWithConstructorInjection {
    public static void main(String[] args) {
        // Создаем контекст Spring на основе нашего конфигурационного класса
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        // Получаем готовый бин (Spring сам создаст его и внедрит провайдер)
        MessageRenderer renderer = context.getBean(MessageRenderer.class);
        renderer.render();
        
        context.close();
    }
}