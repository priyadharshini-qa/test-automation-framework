package com.saucedemo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* Reads tabular test data from .xlsx files.
   First row of the sheet is treated as the header row (column names). */
public class ExcelReader {

    // Returns each data row as a Map<columnName, cellValue>
    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Header row missing in sheet '" + sheetName + "'");
            }

            int columnCount = headerRow.getLastCellNum();
            String[] headers = new String[columnCount];
            for (int col = 0; col < columnCount; col++) {
                headers[col] = getCellValueAsString(headerRow.getCell(col));
            }

            for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                Row currentRow = sheet.getRow(rowIdx);
                if (currentRow == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int col = 0; col < columnCount; col++) {
                    String value = getCellValueAsString(currentRow.getCell(col));
                    rowData.put(headers[col], value);
                }
                rows.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return rows;
    }

    // Convenience method: returns a single column's values as a List<String>
    public static List<String> readColumn(String filePath, String sheetName, String columnName) {
        List<String> values = new ArrayList<>();
        for (Map<String, String> row : readSheet(filePath, sheetName)) {
            if (row.containsKey(columnName)) {
                values.add(row.get(columnName));
            }
        }
        return values;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
