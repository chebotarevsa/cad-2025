package ru.bsuedu.cad.lab;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import org.springframework.stereotype.Component;

@Component
public class CategoryCSVParser implements Parser<Category> {
    public List<Category> parse(String textString) throws NumberFormatException, ParseException {
        List<Category> result = new ArrayList<>();

        String[] lines = textString.split("\n");
        String[] data;

        for(int i = 1; i < lines.length - 1; i++){
            data = lines[i].split(",");
            result.add(new Category(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2]));
        }
        return result;
    }
}