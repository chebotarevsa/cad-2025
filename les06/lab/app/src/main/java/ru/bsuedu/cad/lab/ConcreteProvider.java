package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("provider")
public class ConcreteProvider <T> implements Provider <T>{
    final private Reader reader;
    final private Parser parser;

    public ConcreteProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    public List<T> getItems(Class<T> clazz, int indexFile)
    {

        List<T> itemsList = (List<T>) parser.parse(clazz, reader.read(indexFile));;
        // if (clazz == Product.class){
        //     itemsList = (List<T>) parser.parse(Product.class, reader.read(indexFile));
        // }
            
        // else if (clazz == Category.class)
        // {
        //     itemsList = (List<T>) parser.parse(Product.class, reader.read(1));
        // }
            
        return itemsList;
    }
}
