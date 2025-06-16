package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Products")
public class Product {
  // @GeneratedValue(strategy = GenerationType.IDENTITY)

  @Id
  private Integer productId;

  private String name;
  private String description;

  @ManyToOne
  @JoinColumn(name = "categoryId")
  private Category category;

  private BigDecimal price;
  private Integer stockQuantity;
  private String imageUrl;
  private LocalDate createdAt;
  private LocalDate updatedAt;

  @OneToMany(mappedBy = "product")
  private List<OrderDetail> orderDetails = new ArrayList<>();

  public Product() {

  }

  public Product(Integer productId, String name, String description, Category category, BigDecimal price,
      Integer stockQuantity,
      String imageUrl, LocalDate createdAt, LocalDate updatedAt) {
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.category = category;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.imageUrl = imageUrl;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
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

  public Category getCategory() {
    return category;
  }

  public void setCategoryId(Category category) {
    this.category = category;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDate getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDate updatedAt) {
    this.updatedAt = updatedAt;
  }

  // @Override
  // public String toString() {
  // return "Product{" +
  // "id=" + id +
  // ", name='" + name + '\'' +
  // ", description='" + description + '\'' +
  // ", price=" + price +
  // ", stock_quantity=" + stock_quantity +
  // ", image_url='" + image_url + '\'' +
  // ", created_at=" + calendarToString(created_at) +
  // ", updated_at=" + calendarToString(updated_at) +
  // '}';
  // }

  // public String calendarToString(Calendar calendar) {
  // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  // return sdf.format(calendar.getTime());
  // }
}
