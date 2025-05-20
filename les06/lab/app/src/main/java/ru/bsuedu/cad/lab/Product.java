package ru.bsuedu.cad.lab;

import java.util.Date;
import java.math.BigDecimal;

public class Product {
	public int product_id;
	public String name;
	public String description;
	public int category_id;
	public BigDecimal price;
	public int stock_quantity;
	public String image_url;
	public Date created_at;
	public Date updated_at;

	public Product(int productId, String name, String description, int category_id,
			BigDecimal price, int stock_quantity, String image_url, Date created_at, Date updated_at) {
		this.product_id = productId;
		this.name = name;
		this.description = description;
		this.category_id = category_id;
		this.price = price;
		this.stock_quantity = stock_quantity;
		this.image_url = image_url;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
}