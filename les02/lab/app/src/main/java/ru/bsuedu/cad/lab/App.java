package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;;

public class App {
    public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
        ApplicationContext context = new AnnotationConfigApplicationContext(MessageConfiguration.class);
        Renderer mr = context.getBean("renderer", Renderer.class);
        mr.render();
     }
}
