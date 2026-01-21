package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.util.Calendar;

public class Product {
   public int productId;
   public String name;
   public String description;
   public int categoryId;
   public BigDecimal price;
   public int stockQuantity;
   public String imageUrl;
   public Calendar createdAt;
   public Calendar updatedAt;

   public Product(int productId, String name, String description, int categoryId, BigDecimal price, int stockQuantity, String imageUrl, Calendar createdAt, Calendar updatedAt) {
      this.productId = productId;
      this.name = name;
      this.description = description;
      this.categoryId = categoryId;
      this.price = price;
      this.stockQuantity = stockQuantity;
      this.imageUrl = imageUrl;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
   }
}