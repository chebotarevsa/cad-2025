package ru.bsuedu.cad.lab;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("consoleRenderer")
public class ConsoleTableRenderer  implements Renderer{
    // final private ProductProvider productProvider;
    // final private CategoryProvider categoryProvider;

    // public ConsoleTableRenderer(ProductProvider productProvider, CategoryProvider categoryProvider) {
    //     this.productProvider = productProvider;
    //     this.categoryProvider = categoryProvider;
    // }

    final private Provider<Product> productProvider;
    final private Provider<Category> categoryProvider;
    public ConsoleTableRenderer(@Qualifier("productProvider") Provider<Product> productProvider, @Qualifier("categoryProvider") Provider<Category> categoryProvider) 
    {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
    }


    public void render()
    {
        
        List<Product> productsList = productProvider.getItems(Product.class, 0);
        List<Category> categoriesList = categoryProvider.getItems(Category.class, 1);
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
        
        System.out.println();
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < categoriesList.size(); i++)
        {
            System.out.println("| " + categoriesList.get(i).product_id + " | "
                                    + categoriesList.get(i).name + " | "
                                    + categoriesList.get(i).description + " | ");
            System.out.println("---------------------------------------------------------");
        }


    }

    public String calendarToString(Calendar dateCalendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateCalendar.getTime());
    }

}
