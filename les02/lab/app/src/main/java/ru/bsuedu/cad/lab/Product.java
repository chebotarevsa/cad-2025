package ru.bsuedu.cad.lab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    long productId;
    String name;
    String description;
    int categoryId;
    BigDecimal price;
    int stockQuantity;
    String imageUrl;
    Date createdAt;
    Date updatedAt;
}
