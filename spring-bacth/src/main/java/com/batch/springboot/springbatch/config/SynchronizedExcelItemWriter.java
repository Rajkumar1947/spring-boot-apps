package com.batch.springboot.springbatch.config;

import com.batch.springboot.springbatch.model.User;
import com.batch.springboot.springbatch.model.UserDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class SynchronizedExcelItemWriter implements ItemWriter<UserDTO> {

    private final String outputFileName;

    private final Object lock = new Object();
//    volatile int rowNum = 0;

    public SynchronizedExcelItemWriter(String outputFileName) {
        System.out.println("outputFileName:" +outputFileName);
        this.outputFileName = outputFileName;
//        this.outputFileName = outputFileName;

    }

    @Override
    public void write(List<? extends UserDTO> items) throws Exception {
        synchronized (lock) {
            File file = new File(outputFileName);
            XSSFWorkbook workbook;
            XSSFSheet sheet;

            // If the file exists and is not empty, open the existing workbook
            if (file.exists() && file.length() != 0) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fis);
                }
                sheet = workbook.getSheetAt(0);
            } else {
                // Otherwise, create a new workbook and sheet
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Data");
            }

            // Find the next row number to write
            int rowNum = sheet.getLastRowNum() + 1;

            for (UserDTO item : items) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getId());
                row.createCell(1).setCellValue(item.getEmail());
            }

            // Write the workbook back to the file
            try (FileOutputStream fos = new FileOutputStream(outputFileName)) {
                workbook.write(fos);
            }
        }
    }
}


