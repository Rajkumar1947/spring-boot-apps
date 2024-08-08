package com.batch.springboot.springbatch.config.writer;

import com.batch.springboot.springbatch.model.UserDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@StepScope
public class SynchronizedExcelItemWriter implements ItemWriter<UserDTO> {

    private final SXSSFWorkbook workbook;
    private final SXSSFSheet sheet;
    private final String outputFileName;
    private int rowNum = 0;
    private final Object lock = new Object();


    public SynchronizedExcelItemWriter(@Value("#{jobParameters[outputFileName]}") String outputFileName) {
        this.outputFileName = outputFileName;
        this.workbook = new SXSSFWorkbook(100); // Keep 100 rows in memory at a time
        this.sheet = workbook.createSheet("Data");
        createHeaderRow();
    }

    private void createHeaderRow() {
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Email");
    }

    @Override
    public void write(List<? extends UserDTO> items) throws IOException {
        synchronized (lock) {
            for (UserDTO item : items) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getId());
                row.createCell(1).setCellValue(item.getFirstName());
                row.createCell(2).setCellValue(item.getEmail());
            }

            // Flush rows to disk periodically
            if (rowNum % 100 == 0) {
                sheet.flushRows(100); // Retain last 100 rows in memory
            }
        }
    }

    public void close() throws IOException {
        synchronized (lock) {
            System.out.println("file closed:");
            try (FileOutputStream fos = new FileOutputStream(outputFileName)) {
                workbook.write(fos);
            }
            workbook.dispose();
        }
    }
}
