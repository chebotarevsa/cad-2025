package ru.bsuedu.cad.lab.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDTO {

    @JsonProperty("product")
    private String productName;
    @JsonProperty("category")
    private String categoryName;
    @JsonProperty("quantity")
    private int stockQuantity;

    public ProductDTO(String productName, String categoryName, int stockQuantity) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.stockQuantity = stockQuantity;
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
    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
}
