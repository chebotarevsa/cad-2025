package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;

public class ProductDto {

  private Integer productId;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer stockQuantity;
  private String imageUrl;

  public ProductDto() {
  }

  public ProductDto(Integer productId, String name, String description, BigDecimal price, Integer stockQuantity,
      String imageUrl) {
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.imageUrl = imageUrl;
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
}
