package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("HTML")
public class HTMLTableRenderer implements Renderer {

    private ProductProvider provider;

    public HTMLTableRenderer(ProductProvider productProvider){
        provider = productProvider;
    }

    @Override
    public void render() {
        provider.getProducts();
        System.out.println("Вывод в HTML файл");
    }

}
