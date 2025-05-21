package ru.bsuedu.cad.lab;

import java.util.List;

public class ConcreteProductProvider implements ProductProvider {
   private final Reader reader;
   private final Parser parser;

   public ConcreteProductProvider(Reader reader, Parser parser) {
      this.reader = reader;
      this.parser = parser;
   }

   public List<Product> getProducts() {
      List<Product> productsList = this.parser.parse(this.reader.read());
      return productsList;
   }
}