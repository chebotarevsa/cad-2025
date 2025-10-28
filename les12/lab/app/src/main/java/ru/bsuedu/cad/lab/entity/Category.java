package ru.bsuedu.cad.lab.entity;

import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "Categories")
public class Category {
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Integer categoryId;

  private String name;
  private String description;

  @OneToMany(mappedBy = "category")
  private List<Product> products = new ArrayList<>();

  public Category() {

  }

  public Category(Integer categoryId, String name, String description, List<Product> producList) {
    this.categoryId = categoryId;
    this.name = name;
    this.description = description;
    this.products = producList;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
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

  public List<Product> getProducts() {
    return products;
  }

  public void setOrders(List<Product> products) {
    this.products = products;
  }
}
