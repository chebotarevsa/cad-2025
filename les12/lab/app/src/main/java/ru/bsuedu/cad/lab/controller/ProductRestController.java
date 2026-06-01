package ru.bsuedu.cad.lab.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bsuedu.cad.lab.dto.ProductInfoDTO;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<ProductInfoDTO> getAllProducts() {
        logger.info("REST запрос: получение всех продуктов");

        List<Product> products = productRepository.findAll();
        logger.info("Найдено продуктов: {}", products.size());

        return products.stream()
                .map(product -> {
                    String productName = product.getName();

                    String categoryName = "Без категории";
                    if (product.getCategory() != null) {
                        org.hibernate.Hibernate.initialize(product.getCategory());
                        categoryName = product.getCategory().getName();
                    }

                    Integer stockQuantity = product.getStockQuantity();

                    logger.debug("Товар: {}, категория: {}", productName, categoryName);

                    return new ProductInfoDTO(productName, categoryName, stockQuantity);
                })
                .collect(Collectors.toList());
    }
}