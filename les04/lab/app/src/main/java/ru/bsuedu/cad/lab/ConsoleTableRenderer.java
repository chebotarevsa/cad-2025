package ru.bsuedu.cad.lab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleTableRenderer implements Renderer {

	private ProductProvider provider;

	@Autowired
	public ConsoleTableRenderer(ProductProvider provider) {
		this.provider = provider;
	}

	@Override
	public void render() {
		List<Product> products = provider.getProducts();

		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (Product product : products) {
			System.out.println(
					"| " + String.format("%-2d", product.productId) + " | " + String.format("%-33s", product.name) + " | "
							+ String.format("%-38s", product.description) + " | " + product.categoryId + " | "
							+ String.format("%-4.0f", product.price) + " | " + String.format("%-3d", product.stockQuantity)
							+ " | " + String.format("%-39s", product.imageUrl) + "|" + getDate(product.createdAt)
							+ " | " + getDate(product.updatedAt) + " |");
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}
	}

	public String getDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = formatter.format(date);
		return format;
	}
}