package ru.bsuedu.cad.lab.web;

import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductRepository repo;

    public ProductRestController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Product> getAll() {
        return StreamSupport.stream(repo.findAll().spliterator(), false).toList();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return repo.save(product);
    }
}
