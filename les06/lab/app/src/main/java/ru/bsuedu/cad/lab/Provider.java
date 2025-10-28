package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface Provider <T>{
    public default List<T> getEntitees() throws NumberFormatException, IOException, ParseException {
        List<T> categoryList = this.getParser().parse(this.getReader().read());
        return categoryList;
    }

    public Reader getReader();
    public Parser<T> getParser();
}
