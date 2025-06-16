package ru.bsuedu.cad.lab.mapper;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.dto.ProductDto;
import ru.bsuedu.cad.lab.entity.Product;

@Component
public class ProductMapper {
	public ProductDto toDto(Product product) {
		if (product == null) {
			return null;
		}
		return new ProductDto(
				product.getProductId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getStockQuantity(),
				product.getImageUrl());
	}
}