package ru.bsuedu.cad.lab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleTableRenderer implements Renderer {

	private DataProvider<Product> provider;

	@Autowired
	public ConsoleTableRenderer(DataProvider<Product> provider) {
		this.provider = provider;
	}

	@Override
	public void render() {
		List<Product> products = provider.getItems();

		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (Product product : products) {
			System.out.println(
					"| " + String.format("%-2d", product.product_id) + " | " + String.format("%-33s", product.name) + " | "
							+ String.format("%-38s", product.description) + " | " + product.category_id + " | "
							+ String.format("%-4.0f", product.price) + " | " + String.format("%-3d", product.stock_quantity)
							+ " | " + String.format("%-39s", product.image_url) + "|" + getDate(product.created_at)
							+ " | " + getDate(product.updated_at) + " |");
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