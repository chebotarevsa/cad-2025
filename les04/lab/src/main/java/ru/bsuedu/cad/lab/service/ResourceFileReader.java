package ru.bsuedu.cad.lab.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResourceFileReader implements Reader {

    @Value("${product.csv.filename}")
    private String filename;

    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader initialized at: " + java.time.LocalDateTime.now());
    }

    @Override
    public List<String> readLines() {
        try {
            System.out.println("Looking for file: " + filename);
            var stream = getClass().getClassLoader().getResourceAsStream(filename);
            System.out.println("Found file? " + (stream != null));

            if (stream == null) {
                throw new RuntimeException("File not found: " + filename);
            }

            try (var in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                return in.lines().skip(1).collect(Collectors.toList());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV", e);
        }
    }
}
