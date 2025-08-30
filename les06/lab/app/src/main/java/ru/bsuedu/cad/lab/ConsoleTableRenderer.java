package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("renderer")
public class ConsoleTableRenderer implements Renderer{

    @Override
    public void render() {
        var product = productProvider.getItems();
        System.out.println(product);
    }
    final private Provider<Product> productProvider;
    public ConsoleTableRenderer(Provider<Product> productProvider) {
        this.productProvider = productProvider;
    }
    
}
