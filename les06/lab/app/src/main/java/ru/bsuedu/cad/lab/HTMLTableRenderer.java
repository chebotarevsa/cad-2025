package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("HTML")
public class HTMLTableRenderer implements Renderer {

    @Override
    public void render() {
        var product = productProvider.getItems();
        System.out.println("HTML");
        System.out.println(product);
    }
    final private Provider<Product> productProvider;
    public HTMLTableRenderer(Provider<Product> productProvider) {
        this.productProvider = productProvider;
    }
    
}
