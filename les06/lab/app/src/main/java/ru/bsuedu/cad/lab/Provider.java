package ru.bsuedu.cad.lab;

import java.util.List;

public interface Provider <T>{
      public default List<T> getEntitees() {
         List<T> categoryList = this.getParser().parse(this.getReader().read());
         return categoryList;
      }

      public Reader getReader();
      public Parser<T> getParser();
}