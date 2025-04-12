package ru.bsuedu.cad.lab;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CSVCategoryParser implements Parser<Category>, Mapper{

    @Override
    public List<Category> parse(String text) {
        List<Category> categoriesList = new ArrayList<>();

        String[] lines = text.split("\n");
        for (int i = 1; i < lines.length; i++){
            String[] params = lines[i].split(",");
            categoriesList.add(new Category(
            stringToInteger(params[0]), 
                params[1], 
                params[2]));
        }

        return categoriesList;
    }
    
}
