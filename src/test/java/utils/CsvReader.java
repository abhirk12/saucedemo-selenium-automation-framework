package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static Object[][] readLoginData(String filePath) {
        return readGenericCsv(filePath);
    }

    public static Object[][] readCheckoutData(String filePath) {
        return readGenericCsv(filePath);
    }

    public static Object[][] readGenericCsv(String filePath) {
        List<Object[]> data = new ArrayList<>();

        try (
            InputStream input = CsvReader.class.getClassLoader().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))
        ) {
            // Skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.split(",");
                Object[] row = new Object[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = values[i].trim();
                }
                data.add(row);
            }

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }

        return data.toArray(new Object[0][]);
    }
}