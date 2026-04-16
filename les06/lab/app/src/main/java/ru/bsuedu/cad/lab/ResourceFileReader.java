package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Component
public class ResourceFileReader implements Reader, InitializingBean {

    @Override
    public String read(String path) {
        try {
            return Files.readString(Path.of(new ClassPathResource(path).getFile().getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Initializing ResourceFileReader at: " + Instant.now());
    }
}
