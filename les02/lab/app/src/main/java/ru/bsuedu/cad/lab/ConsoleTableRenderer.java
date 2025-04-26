package ru.bsuedu.cad.lab;

import java.io.PrintStream;
import java.util.List;

public class ConsoleTableRenderer implements Renderer {
   private final ProductProvider provider;

   public ConsoleTableRenderer(ProductProvider provider) {
      this.provider = provider;
   }

   public void render() {
      List<Product> productsList = this.provider.getProducts();
      System.out.println("-----------------------------------------------------------------------------------------");

      for(int i = 0; i < productsList.size(); ++i) {
         PrintStream var10000 = System.out;
         int var10001 = ((Product)productsList.get(i)).productId;
         var10000.println("|" + var10001 + " | " + ((Product)productsList.get(i)).name + " | " + ((Product)productsList.get(i)).description + " | " + ((Product)productsList.get(i)).categoryId + " | " + String.valueOf(((Product)productsList.get(i)).price) + " | " + ((Product)productsList.get(i)).stockQuantity + " | " + ((Product)productsList.get(i)).imageUrl + " | " + String.valueOf(((Product)productsList.get(i)).createdAt) + " | " + String.valueOf(((Product)productsList.get(i)).updatedAt) + " | ");
         System.out.println("-----------------------------------------------------------------------------------------");
      }

   }
}
