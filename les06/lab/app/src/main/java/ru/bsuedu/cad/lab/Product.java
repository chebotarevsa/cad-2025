package ru.bsuedu.cad.lab;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Product {
    public int id;
    public String name;
    public String description;
    public int category_id;
    public double price;
    public int stock_quantity;
    public String image_url;
    public Calendar created_at;
    public Calendar updated_at;

    public Product(int id, String name, String description, int category_id, double price, int stock_quantity, String image_url, Calendar created_at, Calendar updated_at)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category_id = category_id;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.image_url = image_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", stock_quantity=" + stock_quantity +
            ", image_url='" + image_url + '\'' +
            ", created_at=" + calendarToString(created_at) +
            ", updated_at=" + calendarToString(updated_at) +
            '}';
        }

    public String calendarToString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
}
