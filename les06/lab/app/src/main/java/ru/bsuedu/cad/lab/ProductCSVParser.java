package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component
public class ProductCSVParser extends CSVParser<Product> {

    @Override
    protected Product parseLine(String[] params) {
        return new Product(
            stringToInteger(params[0]),
            params[1],
            params[2],
            stringToInteger(params[3]),
            stringToDecimal(params[4]),
            stringToInteger(params[5]),
            params[6],
            stringToCalendar(params[7]),
            stringToCalendar(params[8])
        );
    }
}