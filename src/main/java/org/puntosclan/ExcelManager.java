package org.puntosclan;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelManager {

    private static final String EXCEL_FILE_PATH = "puntos.xlsx";

    public void writeToExcel(String playerName, int points) {
        // Verificar si el archivo Excel ya existe
        if (!Files.exists(Paths.get(EXCEL_FILE_PATH))) {
            createExcelFile(); // Si no existe, crear uno nuevo con encabezados
        }

        try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(Paths.get(EXCEL_FILE_PATH)))) {
            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            // Obtener la última fila ocupada
            int lastRowNum = sheet.getLastRowNum();

            // Crear una nueva fila
            Row row = sheet.createRow(lastRowNum + 1);
            row.createCell(0).setCellValue(playerName);
            row.createCell(1).setCellValue(points);

            // Escribir datos en el archivo Excel
            try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FILE_PATH)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createExcelFile() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Puntos");

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Jugador");
            headerRow.createCell(1).setCellValue("Puntos");

            // Escribir datos en el archivo Excel
            try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FILE_PATH)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
