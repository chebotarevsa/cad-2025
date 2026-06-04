package ru.bsuedu.cad.lab.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import ru.bsuedu.cad.lab.Reader;

public class ResourceFileReader implements Reader {

    private final String resourcePath;

    public ResourceFileReader(String resourcePath) {
        this.resourcePath = Objects.requireNonNull(resourcePath, "resourcePath");
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
