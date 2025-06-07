package ru.bsu.cad.lab.reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResourceFileReader implements Reader {
    private final ResourceLoader resourceLoader;
    private String filePath; // Убираем @Value, будем устанавливать через setter

    public ResourceFileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // Добавляем setter для filePath
    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader инициализирован: " + new Date());
        System.out.println("File path: " + filePath); // Добавим для отладки
    }
}