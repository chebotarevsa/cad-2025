package lab1;

import lab1.model.Category;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcreteCategoryProvider {

    @Value("${categories.file}")
    private String fileName;

    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName))
        )) {
            br.readLine(); // пропускаем заголовок

            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");

                Category c = new Category();
                c.setCategoryId(Integer.parseInt(arr[0]));
                c.setName(arr[1]);
                c.setDescription(arr[2]);

                list.add(c);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения category.csv", e);
        }

        return list;
    }
}