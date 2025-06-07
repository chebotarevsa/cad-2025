package ru.bsuedu.cad.lab;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;



public class CSVParser implements Parser {
    public <T> List<T> parse(Class<T> clazz, String text)
    {
        System.out.println();
        List<T> objects = new ArrayList<>();
        try
        {
            
            Constructor<T> constructor = (Constructor<T>) clazz.getConstructors()[0];
            Class<?>[] paramTypes = constructor.getParameterTypes();
    
            String[] lines = text.split("\n");
            for (int i = 1; i < lines.length; i++)
            {
                String[] params = lines[i].split(",");
                
                Object[] objParams = new Object[paramTypes.length];
    
                for (int j = 0; j < paramTypes.length; j++)
                {
                    objParams[j] = convertParam(paramTypes[j], params[j]);
                }
    
                T obj = constructor.newInstance(objParams);
                objects.add(obj);
    
                
            }
            
        }

        catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
           
        return objects;
            
    }

    public Object convertParam(Class<?> paramType, String param)
    {
        if (paramType == int.class || paramType == Integer.class) return stringToInteger(param);
        else if (paramType == BigDecimal.class) return stringToDecimal(param);
        else if (paramType == Calendar.class) return stringToCalendar(param);
        else return param;
    }


    public Calendar stringToCalendar(String dateString)
    {   
        try{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            cal.setTime(sdf.parse(dateString));
            return cal;
        }
        catch (ParseException e)
        {
            return null; // нулевое год месяц день вписать
        }
        
    }

    public BigDecimal stringToDecimal(String priceString)
    {
        return new BigDecimal(priceString);
        
    }

    public Integer stringToInteger(String numberString)
    {
        return Integer.parseInt(numberString);
    }

    

    
    
}
