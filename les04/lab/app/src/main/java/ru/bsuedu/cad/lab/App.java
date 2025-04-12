/*
 * This source file was generated by the Gradle 'init' task
 */
package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        Renderer renderer = ctx.getBean("HTML", Renderer.class);
        renderer.render();
    }
}
