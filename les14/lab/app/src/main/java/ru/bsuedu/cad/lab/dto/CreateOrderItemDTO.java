package ru.bsuedu.cad.lab.dto;

public class CreateOrderItemDTO {
    private int productId;
    private int quantity;
    
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
