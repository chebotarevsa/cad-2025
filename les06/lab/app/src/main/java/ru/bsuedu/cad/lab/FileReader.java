package ru.bsuedu.cad.lab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public abstract class FileReader implements Reader {

    protected String path;

    public FileReader(PropertyProvider propertyProvider) {
        this.path = getFilePath(propertyProvider);
    }

    protected abstract String getFilePath(PropertyProvider propertyProvider);

    public String read() {
        Resource resource = new ClassPathResource(path);

        try {
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}