package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public interface Mapper {
    default public Date stringToCalendar(String dateString)
    {   
        try{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            cal.setTime(sdf.parse(dateString));
            return cal.getTime();
        }
        catch (ParseException e)
        {
            return null; // нулевое год месяц день вписать
        }

    }

    default public BigDecimal stringToDecimal(String priceString)
    {
        return new BigDecimal(priceString);

    }

    default public Integer stringToInteger(String numberString)
    {
        return Integer.parseInt(numberString);
    }
}
