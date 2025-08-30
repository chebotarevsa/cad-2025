package ru.bsuedu.cad.lab.dto;

public class CreateOrderDetailDto {
	private int productId;
	private int quantity;

	public CreateOrderDetailDto() {
	}

	public CreateOrderDetailDto(int productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

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