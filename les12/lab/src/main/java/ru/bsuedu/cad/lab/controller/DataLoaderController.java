package ru.bsuedu.cad.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class DataLoaderController {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @GetMapping("/load-data")
    @ResponseBody
    public String loadData() {
        if (!productRepo.findAll().isEmpty()) {
            return "Data already exists";
        }

        Category cat1 = new Category("Dog Food", "Dog food products");
        Category cat2 = new Category("Cat Toys", "Toys for cats");

        categoryRepo.save(cat1);
        categoryRepo.save(cat2);

        Product p1 = new Product("Dry dog food", "Complete food", new BigDecimal("1500"), 50, null, LocalDate.now(), LocalDate.now(), cat1);
        Product p2 = new Product("Cat toy", "Soft toy", new BigDecimal("300"), 200, null, LocalDate.now(), LocalDate.now(), cat2);

        productRepo.save(p1);
        productRepo.save(p2);

        Customer c1 = new Customer("Alexey Ivanov", "alex@example.com", "+79261112233", "Moscow");
        Customer c2 = new Customer("Maria Smirnova", "maria@example.com", "+79151234567", "Saint Petersburg");

        customerRepo.save(c1);
        customerRepo.save(c2);

        return "Data loaded successfully! Products: " + productRepo.findAll().size() + ", Customers: " + customerRepo.findAll().size();
    }
}