package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    public static Object[][] readLoginData(String filePath) {

        List<Object[]> data = new ArrayList<>();

        try (FileInputStream fileInputStream =
                     new FileInputStream(filePath);
             Workbook workbook =
                     new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                String username =
                        row.getCell(0).toString().trim();

                String password =
                        row.getCell(1).toString().trim();

                data.add(new Object[] {
                        username,
                        password
                });
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read Excel file",
                    e
            );
        }

        return data.toArray(new Object[0][]);
    }
}