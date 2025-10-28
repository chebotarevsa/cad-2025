package ru.bsuedu.cad.lab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Component;

@Component
public class CSVParser implements Parser {
    public List<Product> parse(String textString) throws NumberFormatException, ParseException {
        List<Product> result = new ArrayList<>();

        String[] lines = textString.split("\n");
        String[] data;

        for(int i = 1; i < lines.length - 1; i++){
            data = lines[i].split(",");
            result.add(new Product(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    Integer.parseInt(data[3]),
                    Double.parseDouble(data[4]),
                    Integer.parseInt(data[5]),
                    data[6],
                    stringToCalendar(data[7]),
                    stringToCalendar(data[8])));
        }

        return result;
    }

    public Calendar stringToCalendar(String dataString) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(dataString));
        return cal;
    }
}