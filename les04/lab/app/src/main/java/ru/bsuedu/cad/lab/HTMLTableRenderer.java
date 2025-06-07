package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("HTML")
public class HTMLTableRenderer implements Renderer {

    @Override
    public void render() {
        var product = productProvider.getProducts();
        System.out.println("HTML");
        System.out.println(product);
    }
    final private ProductProvider productProvider;
    public HTMLTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }
}