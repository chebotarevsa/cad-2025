package ru.bsuedu.cad.lab.reader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ResourceFileReader implements Reader {
    private final String fileName;

    public ResourceFileReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String read() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) throw new RuntimeException("Файл не найден: " + fileName);

        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
