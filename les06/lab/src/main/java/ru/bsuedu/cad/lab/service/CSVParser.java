
package ru.bsuedu.cad.lab.service;

import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.model.Product;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {
    public static List<Product> readProducts(String path) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(CSVParser.class.getResourceAsStream(path)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");
                Product p = new Product(
                    Integer.parseInt(tokens[0]),
                    tokens[1],
                    tokens[2],
                    Integer.parseInt(tokens[3]),
                    Double.parseDouble(tokens[4]),
                    Integer.parseInt(tokens[5]),
                    tokens[6]
                );
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public static List<Category> readCategories(String path) {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(CSVParser.class.getResourceAsStream(path)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");
                categories.add(new Category(Integer.parseInt(tokens[0]), tokens[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}
