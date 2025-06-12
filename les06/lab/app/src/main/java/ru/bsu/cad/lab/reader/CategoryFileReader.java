package ru.bsu.cad.lab.reader;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Qualifier("categoryReader")
public class CategoryFileReader implements Reader {
    private final ResourceLoader resourceLoader;
    
    @Value("${category.file}")
    private String filePath;

    public CategoryFileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public String read() {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
}