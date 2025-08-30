package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component("productProvider")
public class ConcreteProductProvider implements Provider<Product> {

   private final Reader reader;
   private final Parser<Product> parser;

   public ConcreteProductProvider(@Qualifier("productReader") Reader reader, Parser<Product> parser) {
      this.reader = reader;
      this.parser = parser;
   }

   @Override
   public Reader getReader() {
       return this.reader;
   }

   @Override
   public Parser<Product> getParser() {
       return this.parser;
   }
}