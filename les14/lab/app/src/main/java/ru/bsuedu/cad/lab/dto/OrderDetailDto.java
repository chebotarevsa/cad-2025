package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;

public class OrderDetailDto {
	private Integer orderDetailId;
	private int quantity;
	private BigDecimal price;
	private ProductDto product;

	public OrderDetailDto() {
	}

	public OrderDetailDto(Integer orderDetailId, int quantity, BigDecimal price, ProductDto product) {
		this.orderDetailId = orderDetailId;
		this.quantity = quantity;
		this.price = price;
		this.product = product;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}
}
