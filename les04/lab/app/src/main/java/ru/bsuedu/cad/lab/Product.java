package ru.bsuedu.cad.lab;

import java.util.Date;
import java.math.BigDecimal;

public class Product {
	public long productId;
	public String name;
	public String description;
	public int categoryId;
	public BigDecimal price;
	public int stockQuantity;
	public String imageUrl;
	public Date createdAt;
	public Date updatedAt;

	public Product(long productId, String name, String description, int categoryId,
			BigDecimal price, int stockQuantity, String imageUrl, Date createdAt, Date updatedAt) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.categoryId = categoryId;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
