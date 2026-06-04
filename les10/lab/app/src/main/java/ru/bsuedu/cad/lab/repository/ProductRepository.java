package ru.bsuedu.cad.lab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product create(Product product) {
        return save(product);
    }

    @Override
    Optional<Product> findById(Long id);

    @Override
    List<Product> findAll();
}
