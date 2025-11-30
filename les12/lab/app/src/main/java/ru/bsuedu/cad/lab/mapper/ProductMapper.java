package ru.bsuedu.cad.lab.mapper;

import ru.bsuedu.cad.lab.dto.ProductDto;
import ru.bsuedu.cad.lab.entity.Product;

public class ProductMapper {
    public static ProductDto toDto(Product product)
    {
        return new ProductDto(product.getName(), product.getCategory().getName(), product.getStockQuantity());
    }
}
