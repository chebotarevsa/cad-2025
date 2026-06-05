package org.example;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceFileReader implements Reader {

    private String fileName;

    public ResourceFileReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String read() {
        StringBuilder result = new StringBuilder();

        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (inputStream == null) {
                System.out.println("Файл не найден: " + fileName);
                return "";
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8)
            );

            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }

        return result.toString();
    }
}