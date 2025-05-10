package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class CSVProductParser implements Parser<Product>, Mapper{

    @Override
    public List<Product> parse(String text) {
        List<Product> productsList = new ArrayList<>();

        String[] lines = text.split("\n");
        for (int i = 1; i < lines.length; i++)
        {
            String[] params = lines[i].split(",");
            productsList.add(new Product(
                stringToInteger(params[0]), 
                params[1], 
                params[2], 
                stringToInteger(params[3]), 
                stringToDecimal(params[4]),
                stringToInteger(params[5]),
                params[6],
                stringToCalendar(params[7]),
                stringToCalendar(params[8])));
        }
        return productsList;
    }

    
}
