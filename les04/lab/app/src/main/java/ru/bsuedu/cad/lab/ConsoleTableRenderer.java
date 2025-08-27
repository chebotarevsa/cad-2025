package ru.bsuedu.cad.lab;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;

@Component("consoleRenderer")
public class ConsoleTableRenderer  implements Renderer{
    final private ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider1) {
        this.provider = provider1;
    }

    public void render()
    {
        List<Product> productsList = provider.getProducts();
        
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < productsList.size(); i++)
        {
            System.out.println("| " + productsList.get(i).product_id + " | "
                                    + productsList.get(i).name + " | "
                                    + productsList.get(i).description + " | "
                                    + productsList.get(i).category_id + " | "
                                    + productsList.get(i).price + " | "
                                    + productsList.get(i).stock_quantity + " | "
                                    + productsList.get(i).image_url + " | "
                                    + calendarToString(productsList.get(i).created_at) + " | "
                                    + calendarToString(productsList.get(i).updated_at) + " | ");
            System.out.println("---------------------------------------------------------");
        }
    }

    public String calendarToString(Calendar dateCalendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateCalendar.getTime());
    }

}
