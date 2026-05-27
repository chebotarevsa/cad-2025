package laba;

import java.io.InputStream;
import java.util.Scanner;

public class CarServiceMessageProvider implements MessageProvider {
    @Override
    public String getMessage() {
        String result = "========================================\n";
        result += "           AUTO PARTS FOR SALE          \n";
        result += "========================================\n";

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("items.csv");
            if (is == null) {
                return "Oshibka: items.csv ne naiden!";
            }
            Scanner scanner = new Scanner(is);
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    result += parts[0] + ". " + parts[1] + " | Price: " + parts[2] + "\n";
                }
            }
            scanner.close();
        } catch (Exception e) {
            return "File error!";
        }
        result += "========================================\n";
        return result;
    }
}
