package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("productReader")
public class ProductFileReader extends FileReader {

    public ProductFileReader(PropertyProvider propertyProvider) {
        super(propertyProvider);
    }

    @Override
    protected String getFilePath(PropertyProvider propertyProvider) {
        return propertyProvider.getProductFileName();
    }
}