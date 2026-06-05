package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@WebServlet("/load-data")
public class CSVDataLoaderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        CategoryRepository categoryRepo = (CategoryRepository)
                getServletContext().getAttribute("categoryRepo");
        ProductRepository productRepo = (ProductRepository)
                getServletContext().getAttribute("productRepo");
        CustomerRepository customerRepo = (CustomerRepository)
                getServletContext().getAttribute("customerRepo");

        resp.setContentType("text/html;charset=UTF-8");

        if (!productRepo.findAll().isEmpty()) {
            resp.getWriter().println("<h3>Data already exists</h3>");
            resp.getWriter().println("<a href='create-order'>Go to Create Order Form</a>");
            return;
        }

        Category cat1 = new Category("Dog Food", "Dog food products");
        Category cat2 = new Category("Cat Toys", "Toys for cats");
        Category cat3 = new Category("Dog Care", "Care products for dogs");

        categoryRepo.save(cat1);
        categoryRepo.save(cat2);
        categoryRepo.save(cat3);

        Product p1 = new Product("Dry dog food", "Complete food for adult dogs", new BigDecimal("1500"), 50, null, LocalDate.now(), LocalDate.now(), cat1);
        Product p2 = new Product("Cat toy Mouse", "Soft toy for playing", new BigDecimal("300"), 200, null, LocalDate.now(), LocalDate.now(), cat2);
        Product p3 = new Product("Dog shampoo", "Hypoallergenic shampoo", new BigDecimal("550"), 35, null, LocalDate.now(), LocalDate.now(), cat3);

        productRepo.save(p1);
        productRepo.save(p2);
        productRepo.save(p3);

        Customer c1 = new Customer("Alexey Ivanov", "alex@example.com", "+79261112233", "Moscow");
        Customer c2 = new Customer("Maria Smirnova", "maria@example.com", "+79151234567", "Saint Petersburg");

        customerRepo.save(c1);
        customerRepo.save(c2);

        resp.getWriter().println("<h3>Data loaded successfully!</h3>");
        resp.getWriter().println("<p>Products: 3</p>");
        resp.getWriter().println("<p>Customers: 2</p>");
        resp.getWriter().println("<br><a href='create-order'>Go to Create Order Form</a>");
        resp.getWriter().println("&nbsp;&nbsp;<a href='orders'>View Orders</a>");
    }
}