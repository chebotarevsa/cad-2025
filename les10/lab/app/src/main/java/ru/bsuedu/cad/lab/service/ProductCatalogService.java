package ru.bsuedu.cad.lab.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class ProductCatalogService {

    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    @Transactional(readOnly = true)
    public List<ProductView> findAllProductViews() {
        return productRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    private ProductView toView(Product product) {
        return new ProductView(
                product.getName(),
                product.getCategory().getName(),
                product.getStockQuantity());
    }
}
