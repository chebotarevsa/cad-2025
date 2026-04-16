package ru.bsuedu.cad.lab.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", repo.findAll());
        return "products";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "create-product";
    }

    @PostMapping("/create")
    public String create(Product product) {
        repo.save(product);
        return "redirect:/products";
    }
}
