package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CSVParser implements Parser {
   public CSVParser() {
   }

   public List<Product> parse(String text) {
      List<Product> productsList = new ArrayList();
      String[] lines = text.split("\n");

      for(int i = 1; i < lines.length; ++i) {
         String[] params = lines[i].split(",");
         productsList.add(new Product(this.stringToInteger(params[0]), params[1], params[2], this.stringToInteger(params[3]), this.stringToDecimal(params[4]), this.stringToInteger(params[5]), params[6], this.stringToCalendar(params[7]), this.stringToCalendar(params[8])));
      }

      return productsList;
   }

   public Calendar stringToCalendar(String dateString) {
      try {
         Calendar cal = Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
         cal.setTime(sdf.parse(dateString));
         return cal;
      } catch (ParseException var4) {
         return null;
      }
   }

   public BigDecimal stringToDecimal(String priceString) {
      return new BigDecimal(priceString);
   }

   public Integer stringToInteger(String numberString) {
      return Integer.parseInt(numberString);
   }
}

