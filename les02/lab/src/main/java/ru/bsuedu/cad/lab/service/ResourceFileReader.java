package ru.bsuedu.cad.lab.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceFileReader implements Reader {
    private final String filename;

    public ResourceFileReader(String filename) {
        this.filename = filename;
    }

    @Override
    public List<String> readLines() {
        try (var reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filename)))) {
            return reader.lines().skip(1).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + filename, e);
        }
    }
}
