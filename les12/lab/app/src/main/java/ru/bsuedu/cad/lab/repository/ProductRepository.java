package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.lab.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
