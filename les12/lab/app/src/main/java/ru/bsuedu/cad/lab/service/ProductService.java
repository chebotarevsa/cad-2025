package ru.bsuedu.cad.lab.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class ProductService {
    final private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(){
        List<Product> productList = new ArrayList<>();
        for(Product i : productRepository.findAll())
        {
            productList.add(i);
           
        }
        return productList;

    }
}
