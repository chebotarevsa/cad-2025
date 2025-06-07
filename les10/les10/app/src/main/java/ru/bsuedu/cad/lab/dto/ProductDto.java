package ru.bsuedu.cad.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDto {
    @JsonProperty
    private String productName;
    @JsonProperty
    private String categoryName;
    @JsonProperty
    private int amount;

    public ProductDto(String productName, String categoryName, int amount) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
