package ru.bsuedu.cad.lab;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ResourceFileReader implements Reader
{
    private final String filename;
    public ResourceFileReader(String filename) {
        this.filename = filename;
    }
    @Override
    public List<String> read() {
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            return reader.lines().toList();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла", e);
        }
    }
}