package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public abstract class CSVParser<T> implements Parser<T> {

    @Override
    public List<T> parse(String text) {
        List<T> resultList = new ArrayList<>();
        String[] lines = text.split("\n");

        for (int i = 1; i < lines.length; ++i) {
            String[] params = lines[i].split(",");
            resultList.add(parseLine(params));
        }

        return resultList;
    }

    protected abstract T parseLine(String[] params);

    protected Integer stringToInteger(String numberString) {
        return Integer.parseInt(numberString);
    }

    protected BigDecimal stringToDecimal(String priceString) {
        return new BigDecimal(priceString);
    }

    protected Calendar stringToCalendar(String dateString) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            cal.setTime(sdf.parse(dateString));
            return cal;
        } catch (ParseException e) {
            return null;
        }
    }
}