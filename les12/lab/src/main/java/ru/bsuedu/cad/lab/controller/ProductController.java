package ru.bsuedu.cad.lab.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public List<Map<String, Object>> getProducts() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", p.getName());
            map.put("categoryName", p.getCategory() != null ? p.getCategory().getName() : "Без категории");
            map.put("stockQuantity", 100);
            result.add(map);
        }
        return result;
    }
}