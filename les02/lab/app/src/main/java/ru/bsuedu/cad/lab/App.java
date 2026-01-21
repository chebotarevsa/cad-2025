package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
   public App() {
   }

   public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(new Class[]{Config.class});
      Renderer renderer = (Renderer)context.getBean("renderer", Renderer.class);
      renderer.render();
   }
}
