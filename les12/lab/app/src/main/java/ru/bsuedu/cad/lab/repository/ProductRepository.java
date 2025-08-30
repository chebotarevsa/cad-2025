package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}