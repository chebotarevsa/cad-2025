package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findByCategoryId(Long categoryId);
}