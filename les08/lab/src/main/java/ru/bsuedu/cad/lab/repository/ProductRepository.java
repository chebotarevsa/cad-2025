package ru.bsuedu.cad.lab.repository;

import ru.bsuedu.cad.lab.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
