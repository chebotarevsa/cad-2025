package ru.bsuedu.cad.lab;

import java.security.Provider;

import org.springframework.stereotype.Component;

@Component("htmlRenderer")
public class HTMLTableRenderer implements Renderer{
    private ProductProvider provider;

    public HTMLTableRenderer(ProductProvider productProvider){
        provider = productProvider;
    }

    @Override
    public void render() {
        provider.getProducts();
        
        System.out.println("HTML");
    }
    
}
