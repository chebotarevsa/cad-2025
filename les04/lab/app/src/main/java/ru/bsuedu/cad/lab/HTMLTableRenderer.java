package ru.bsuedu.cad.lab;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("htmlTableRenderer")
public class HTMLTableRenderer implements Renderer {

	private ProductProvider provider;

	@Autowired
	public HTMLTableRenderer(ProductProvider provider) {
		this.provider = provider;
	}

	@Override
	public void render() {
		List<Product> products = provider.getProducts();

		File file = new File("src/main/resources/output.html");
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("""
				<!DOCTYPE html>
				<html lang="ru">
				<head>
					<meta charset="UTF-8">
					<title>HTML</title>
					<style>
					table {
						border-collapse: collapse;
						width: auto;
					}
						th, td{
							border: 1px solid black;
							padding: 3px;
							text-align: center;
						}
					</style>
				</head>
				<body lang="ru">
					<table>
						<tr>
							<th>product_id</th>
							<th>name</th>
							<th>description</th>
							<th>category_id</th>
							<th>price</th>
							<th>stock_quantity</th>
							<th>image_url</th>
							<th>created_at</th>
							<th>updated_at</th>
						</tr>
				""");

		for (Product product : products) {
			htmlBuilder.append("<tr>")
					.append("<td>").append(String.format("%-2d", product.productId)).append("</td>")
					.append("<td>").append(String.format("%-33s", product.name)).append("</td>")
					.append("<td>").append(String.format("%-38s", product.description)).append("</td>")
					.append("<td>").append(product.categoryId).append("</td>")
					.append("<td>").append(String.format("%-4.0f", product.price)).append("</td>")
					.append("<td>").append(String.format("%-3d", product.stockQuantity)).append("</td>")
					.append("<td>").append(String.format("%-39s", product.imageUrl)).append("</td>")
					.append("<td>").append(new SimpleDateFormat("yyyy-MM-dd").format(product.createdAt)).append("</td>")
					.append("<td>").append(new SimpleDateFormat("yyyy-MM-dd").format(product.updatedAt)).append("</td>")
					.append("</tr>");
		}

		htmlBuilder.append("""
					</table>
				</body>
				</html>
				""");

		try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
			writer.println(htmlBuilder.toString());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();

				if (file.exists()) {
					desktop.browse(file.toURI());
				} else {
					System.err.println("Файл не найден");
				}
			} else {
				System.err.println("Desktop API не поддерживается");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
