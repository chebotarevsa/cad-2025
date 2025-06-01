package ru.bsuedu.cad.lab.entity;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
@Table(name = "products")
public class Product {
   @Id
   @Column(name = "product_id")
   private Long productId;
   private String name;
   private String description;
   private BigDecimal price;
   @Column(name = "stock_quantity")
   private int stockQuantity;
   @Column(name = "image_url")
   private String imageUrl;
   @Column(name = "created_at")
   private Date createdAt;
   @Column(name = "updated_at")
   private Date updatedAt;

   @ManyToOne
   @JoinColumn(name = "category_id")
   private Category category;

   public Product(){
        
   }

   public Product(Long productId, String name, String description, BigDecimal price, int stockQuantity,
      String imageUrl, Date createdAt, Date updatedAt, Category category) {
   this.productId = productId;
   this.name = name;
   this.description = description;
   this.price = price;
   this.stockQuantity = stockQuantity;
   this.imageUrl = imageUrl;
   this.createdAt = createdAt;
   this.updatedAt = updatedAt;
   this.category = category;
   }


   public Long getProductId() {
      return productId;
   }


   public void setProductId(Long productId) {
      this.productId = productId;
   }


   public String getName() {
      return name;
   }


   public void setName(String name) {
      this.name = name;
   }


   public String getDescription() {
      return description;
   }


   public void setDescription(String description) {
      this.description = description;
   }


   public BigDecimal getPrice() {
      return price;
   }


   public void setPrice(BigDecimal price) {
      this.price = price;
   }


   public int getStockQuantity() {
      return stockQuantity;
   }


   public void setStockQuantity(int stockQuantity) {
      this.stockQuantity = stockQuantity;
   }


   public String getImageUrl() {
      return imageUrl;
   }


   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }


   public Date getCreatedAt() {
      return createdAt;
   }


   public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
   }


   public Date getUpdatedAt() {
      return updatedAt;
   }


   public void setUpdatedAt(Date updatedAt) {
      this.updatedAt = updatedAt;
   }


   public Category getCategory() {
      return category;
   }


   public void setCategory(Category category) {
      this.category = category;
   } 
   
}

