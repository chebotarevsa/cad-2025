package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class App {

    
    public static void main(String[] args) {

        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Ошибка установки кодировки UTF-8: " + e.getMessage());
        }

        
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        Renderer renderer = ctx.getBean(Renderer.class);
        renderer.render();
        
    }
    
    
}
