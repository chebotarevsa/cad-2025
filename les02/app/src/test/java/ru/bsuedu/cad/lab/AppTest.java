package ru.bsuedu.cad.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ru.bsuedu.cad.lab.impl.CSVParser;

class AppTest {

    @Test
    void csvParserReadsProductsFromHeaderAndDataLines() {
        String csv = """
            product_id,name,description,category_id,price,stock_quantity,image_url,created_at,updated_at
            1,Товар,Описание,2,100,5,https://example.com/img.jpg,2025-01-15,2025-02-01
            """;

        List<Product> products = new CSVParser().parse(csv);

        assertEquals(1, products.size());
        assertEquals(1L, products.get(0).getProductId());
        assertEquals("Товар", products.get(0).getName());
        assertEquals(2, products.get(0).getCategoryId());
    }
}
