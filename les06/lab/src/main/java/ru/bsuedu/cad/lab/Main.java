
package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        Renderer renderer = context.getBean(Renderer.class);
        renderer.render();
        context.close();
    }
}
