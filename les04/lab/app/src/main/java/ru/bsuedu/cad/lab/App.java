package ru.bsuedu.cad.lab;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
   public App() {
   }

   public static void main(String[] args) {
   
      ApplicationContext context = new AnnotationConfigApplicationContext(new Class[]{Config.class});
      Renderer renderer = (Renderer)context.getBean("HTML", Renderer.class);
      
      renderer.render();
      
   }
}