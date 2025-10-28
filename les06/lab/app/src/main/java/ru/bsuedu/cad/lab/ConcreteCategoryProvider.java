package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//"Main" - класс, точка входа в приложение

@Component("categoryProvider")
public class ConcreteCategoryProvider implements Provider<Category>{
    private Reader reader;
    private Parser<Category> parser;
    
    public ConcreteCategoryProvider(@Qualifier("categoryReader")Reader reader, Parser<Category> parser){
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public Reader getReader() {
        return this.reader;
    }

    @Override
    public Parser<Category> getParser() {
        return this.parser;
    }

    // public List<Category> getCategory(){
    //     List<Category> list = parser.parse(reader.read());
    //     return list;
    // }
}
