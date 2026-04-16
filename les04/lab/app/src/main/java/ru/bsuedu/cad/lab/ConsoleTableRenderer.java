package ru.bsuedu.cad.lab;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleTableRenderer implements Renderer{
    private final ProductProvider productProvider;

    @Override
    public void render() {
        System.out.println("product_id|name|description|category_id|price|stock_quantity|image_url|created_at|updated_at");
        productProvider.getProducts().forEach(s -> System.out.println(s.productId + "|" + s.description + "|" + s.categoryId + "|" + s.price + "|" + s.stockQuantity + "|" + s.imageUrl + "|" + s.createdAt + "|" + s.updatedAt));
    }
}
