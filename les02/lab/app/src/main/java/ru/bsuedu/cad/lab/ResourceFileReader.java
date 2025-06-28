package ru.bsuedu.cad.lab;

import org.springframework.core.io.Resource;
import java.io.IOException;

public class ResourceFileReader implements Reader {
    private final Resource resource;

    public ResourceFileReader(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String read() {
        try {
            return new String(resource.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource", e);
        }
    }
}