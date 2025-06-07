package ru.bsuedu.cad.lab.mapper;

import ru.bsuedu.cad.lab.dto.ProductDTO;
import ru.bsuedu.cad.lab.entity.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product p){
        return new ProductDTO(p.getName(), p.getCategory().getName(), p.getStockQuantity());
    }
}
