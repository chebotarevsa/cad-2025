package ru.bsuedu.cad.lab;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceFileReader implements Reader {

    @Override
    public String read() {
        try {
            return Files.readString(Path.of(new ClassPathResource("data.csv").getFile().getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
