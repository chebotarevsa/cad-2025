package ru.bsuedu.cad.lab.dto;

public class ProductInfoDTO {
    private String productName;
    private String categoryName;
    private Integer stockQuantity;

    public ProductInfoDTO(String productName, String categoryName, Integer stockQuantity) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.stockQuantity = stockQuantity;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}