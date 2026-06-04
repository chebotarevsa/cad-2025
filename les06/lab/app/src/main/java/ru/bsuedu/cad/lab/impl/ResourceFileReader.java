package ru.bsuedu.cad.lab.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.Reader;

@Component
public class ResourceFileReader implements Reader, InitializingBean {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("#{environment.getProperty('products.filename')}")
    private String resourcePath;

    @Override
    public void afterPropertiesSet() {
        System.out.println(
                "ResourceFileReader полностью инициализирован: " + LocalDateTime.now().format(FORMATTER));
    }

    @Override
    public String read() {
        try (InputStream inputStream = ResourceFileReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + resourcePath, e);
        }
    }
}
