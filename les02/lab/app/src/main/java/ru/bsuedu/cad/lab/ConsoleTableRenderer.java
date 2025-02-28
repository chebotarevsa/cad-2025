package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ConsoleTableRenderer implements Renderer {
    private ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    public void render() throws NumberFormatException, IOException, ParseException{
        List<Product> tabList = provider.getProducts();

        System.out.println("_____________________________________________________");
        for(int i = 0; i < tabList.size(); i++){
            System.out.println("|" + tabList.get(i).product_id + " | "
                                   + tabList.get(i).name + " | "
                                   + tabList.get(i).description + " | "
                                   + tabList.get(i).category_id + " | "
                                   + tabList.get(i).price + " | "
                                   + tabList.get(i).stock_quantity + " | "
                                   + tabList.get(i).image_url + " | "
                                   + calendarToString(tabList.get(i).created_at) + " | "
                                   + calendarToString(tabList.get(i).updated_at) + " | "
            );
            System.out.println("_____________________________________________________");
        }
    }

    public String calendarToString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
}
