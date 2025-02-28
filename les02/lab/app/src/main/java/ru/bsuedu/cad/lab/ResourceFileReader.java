package ru.bsuedu.cad.lab;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class ResourceFileReader implements Reader {

    private String path;

    public ResourceFileReader() {
        try {
            path = Paths.get(ClassLoader.getSystemResource("product.csv").toURI()).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка загрузки файла resources/product.csv", e);
        }
    }

    public String read(){
        try(FileReader reader = new FileReader(path)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer result = new StringBuffer("");
            String line;

            do {
                line = bufferedReader.readLine();
                result.append(line);
                result.append("\n");
                
            } while (line != null);
            return result.toString();

        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }    
}