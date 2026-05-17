package ru.bsuedu.cad.lab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public List<Map<String, Object>> getProducts() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return products.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", p.getName());
            map.put("category", p.getCategory().getName());
            map.put("stockQuantity", p.getStockQuantity());
            return map;
        }).toList();
    }
}