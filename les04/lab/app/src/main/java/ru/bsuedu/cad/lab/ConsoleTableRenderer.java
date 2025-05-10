package ru.bsuedu.cad.lab;

import java.io.PrintStream;
import java.util.List;

import org.springframework.stereotype.Component;


@Component("renderer")
public class ConsoleTableRenderer implements Renderer {
   private final ProductProvider provider;

   public ConsoleTableRenderer(ProductProvider provider1) {
      this.provider = provider1;
   }

   public void render() {
      List<Product> productsList = this.provider.getProducts();
      System.out.println("[----------------------------------------------------------------------------------]");

      for(int i = 0; i < productsList.size(); ++i) {
         PrintStream var10000 = System.out;
         int var10001 = ((Product)productsList.get(i)).getProductID();
         var10000.println("|" 
            + var10001 + " | " 
            + ((Product)productsList.get(i)).getName() + " | " 
            + ((Product)productsList.get(i)).getDescription() + " | " 
            + ((Product)productsList.get(i)).getCategoryID() + " | " 
            + String.valueOf(((Product)productsList.get(i)).getPrice()) + " | " 
            + ((Product)productsList.get(i)).getStock_quantity() + " | " 
            + ((Product)productsList.get(i)).getImageURL() + " | " 
            + String.valueOf(((Product)productsList.get(i)).getCreatedAt()) + " | " 
            + String.valueOf(((Product)productsList.get(i)).getUpdatedAt()) + " | "
         );
         System.out.println("[----------------------------------------------------------------------------------]");
      }
   }
}