package com.batch.springboot.springbatch.config.writer;

import com.batch.springboot.springbatch.model.UserDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelWriter  {

    String outputFileName;
    private static final String[] COLUMN_HEADERS = {"First Name", "Email"};
    private static final String FILE_PATH = "output.xlsx";
    private Workbook workbook = new XSSFWorkbook();
    private Sheet sheet = workbook.createSheet("Customers");
    private int rowIndex = 0;

    public  ExcelWriter() {

    }
    public ExcelWriter(String outputFileName) {
        // Create header row
        this.outputFileName = outputFileName;
        Row headerRow = sheet.createRow(rowIndex++);
        for (int i = 0; i < COLUMN_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(COLUMN_HEADERS[i]);
        }
    }

//    @Override
    public synchronized void write(List<? extends UserDTO> items) throws IOException {
        for (UserDTO user : items) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(user.getFirstName());
            row.createCell(1).setCellValue(user.getEmail());
        }
            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                workbook.write(fos);
            }

    }

    public synchronized void close() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
