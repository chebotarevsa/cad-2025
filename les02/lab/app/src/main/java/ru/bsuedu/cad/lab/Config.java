package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
   basePackages = {"ru.bsuedu.cad.lab"}
)
public class Config {
   public Config() {
   }

   @Bean
   public Parser parser() {
      return new CSVParser();
   }

   @Bean
   public Reader reader() {
      return new ResourceFileReader();
   }

   @Bean
   public ProductProvider provider() {
      ProductProvider provider = new ConcreteProductProvider(this.reader(), this.parser());
      return provider;
   }

   @Bean
   public Renderer renderer() {
      Renderer renderer = new ConsoleTableRenderer(this.provider());
      return renderer;
   }
}