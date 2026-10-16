package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Scanner;

@Component
public class ResourceFileReader implements Reader {

    @Override
    public String read() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("products.csv");
        if (is == null) {
            throw new IllegalStateException("Файл products.csv не найден в resources");
        }
        try (Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}