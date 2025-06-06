package ru.bsuedu.cad.lab.service;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CsvLoaderService {

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CustomerRepository customerRepo;

    public void loadCsvData() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("category.csv").getInputStream()))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                Category cat = new Category();
                cat.setName(line[0]);
                cat.setDescription(line[1]);
                categoryRepo.save(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("product.csv").getInputStream()))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                Product p = new Product();
                p.setName(line[0]);
                p.setDescription(line[1]);
                p.setCategory(categoryRepo.findById(Integer.parseInt(line[2])).orElse(null));
                p.setPrice(new BigDecimal(line[3]));
                p.setStockQuantity(Integer.parseInt(line[4]));
                p.setImageUrl(line[5]);
                p.setCreatedAt(LocalDateTime.parse(line[6]));
                p.setUpdatedAt(LocalDateTime.parse(line[7]));
                productRepo.save(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("customer.csv").getInputStream()))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                Customer c = new Customer();
                c.setName(line[0]);
                c.setEmail(line[1]);
                c.setPhone(line[2]);
                c.setAddress(line[3]);
                customerRepo.save(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
