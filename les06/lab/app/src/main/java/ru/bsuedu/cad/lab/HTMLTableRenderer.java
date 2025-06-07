package ru.bsuedu.cad.lab;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("htmlRenderer")
public class HTMLTableRenderer implements Renderer{
    final private Provider <Product> productProvider;
    final private Provider <Category> categoryProvider;
    public HTMLTableRenderer(@Qualifier("productProvider") Provider<Product> productProvider, @Qualifier("categoryProvider") Provider<Category> categoryProvider) 
    {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
    }


    @Override
    public void render() {
        productProvider.getItems(Product.class, 0);
        categoryProvider.getItems(Category.class, 1);
        
        System.out.println("HTML");
    }
    
}
