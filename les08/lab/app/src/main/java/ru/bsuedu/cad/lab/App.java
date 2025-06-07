package ru.bsuedu.cad.lab;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.bsuedu.cad.lab.app.Config;
import ru.bsuedu.cad.lab.app.OrderClient;

public class App {
   public App() {
   }

   public static void main(String[] args) {
   
      ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
      var orderClient = context.getBean(OrderClient.class);
      orderClient.run();
   }
}
