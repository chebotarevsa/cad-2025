package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("categoryReader")
public class CategoryFileReader extends FileReader {

    public CategoryFileReader(PropertyProvider propertyProvider) {
        super(propertyProvider);
    }

    @Override
    protected String getFilePath(PropertyProvider propertyProvider) {
        return propertyProvider.getCategoryFileName();
    }
}