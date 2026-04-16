package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CSVParser implements Parser {

    @Override
    public List<Product> parse(String data) {
        return Arrays.stream(data.split("\r\n")).skip(1).map(
                row -> {
                    String[] rowValues = row.split(",");
                    String[] firstDate = rowValues[7].split("-");
                    String[] secondDate = rowValues[8].split("-");
                    return new Product(
                            Long.parseLong(rowValues[0]),
                            rowValues[1],
                            rowValues[2],
                            Integer.parseInt(rowValues[3]),
                            new BigDecimal(rowValues[4]),
                            Integer.parseInt(rowValues[5]),
                            rowValues[6],
                            new Date(Integer.parseInt(firstDate[0]), Integer.parseInt(firstDate[1]), Integer.parseInt(firstDate[2])),
                            new Date(Integer.parseInt(secondDate[0]),  Integer.parseInt(secondDate[1]), Integer.parseInt(secondDate[2]))
                    );
                }
        ).toList();
    }
}
