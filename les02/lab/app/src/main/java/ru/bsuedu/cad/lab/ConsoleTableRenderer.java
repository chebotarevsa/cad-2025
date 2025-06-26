package ru.bsuedu.cad.lab;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ConsoleTableRenderer  implements Renderer{
    final private ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    public void render()
    {
        List<Product> productsList = provider.getProducts();
        
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < productsList.size(); i++)
        {
            System.out.println("| " + productsList.get(i).productId + " | "
                                    + productsList.get(i).name + " | "
                                    + productsList.get(i).description + " | "
                                    + productsList.get(i).categoryId + " | "
                                    + productsList.get(i).price + " | "
                                    + productsList.get(i).stockQuantity + " | "
                                    + productsList.get(i).imageUrl + " | "
                                    + calendarToString(productsList.get(i).createdAt) + " | "
                                    + calendarToString(productsList.get(i).updatedAt) + " | ");
            System.out.println("---------------------------------------------------------");
        }
    }

    public String calendarToString(Calendar dateCalendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateCalendar.getTime());
    }

}
