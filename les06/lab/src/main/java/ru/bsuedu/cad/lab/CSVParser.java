package ru.bsuedu.cad.lab;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CSVParser implements Parser<Product> {
    
    @Override
    public List<Product> parse(List<String> lines) {
        List<Product> products = new ArrayList<>();
        
        // Пропускаем заголовок (первую строку)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            // Убираем кавычки в начале и конце, если есть
            line = line.replace("'", "");
            String[] parts = line.split(",");
            
            if (parts.length >= 5) {
                // Индексы: 0 - name, 1 - description, 2 - category_id, 3 - price, 4 - stock_quantity
                String name = parts[0].trim();
                double price = Double.parseDouble(parts[3].trim());
                String category = parts[1].trim(); // используем description как категорию
                int id = i; // временный id
                
                products.add(new Product(id, name, price, category));
            }
        }
        return products;
    }
}