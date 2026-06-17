package lab1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVParser implements Parser {

    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();

        String[] lines = data.split("\n");
        boolean first = true;

        for (String line : lines) {
            if (first) {
                first = false;
                continue;
            }

            String[] parts = line.split(";");
            if (parts.length < 5) continue;

            Product p = new Product();
            p.setProductId(Long.parseLong(parts[0]));
            p.setName(parts[1]);
            p.setDescription(parts[2]);
            p.setCategoryId(Integer.parseInt(parts[3]));
            p.setPrice(new BigDecimal(parts[4]));
            p.setStockQuantity(10);
            p.setImageUrl(null);
            p.setCreatedAt(new Date());
            p.setUpdatedAt(new Date());

            products.add(p);
        }

        return products;
    }
}
