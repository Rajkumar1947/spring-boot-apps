package com.batch.springboot.springbatch.controller;

import com.batch.springboot.springbatch.service.FileDownloadHelper;
import com.batch.springboot.springbatch.service.JobProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JobController {

    private final Job exportUser;
    private final Job importUsers;
    private final JobProcessorService jobProcessorService;
    private final FileDownloadHelper fileDownloadHelper;


    @PostMapping("/import/customers")
    public ResponseEntity<?> importCsvToDBJob() {
        return jobProcessorService.runJob(importUsers, null);
    }

    @PostMapping("/export/user/{lastName}")
    public ResponseEntity<?> exportUser(@PathVariable final String lastName) throws Exception {
        jobProcessorService.runJob(exportUser,lastName);
        String fileName = "output.xlsx";
        FileInputStream fileInputStream = fileDownloadHelper.download(fileName);
        if(null != fileInputStream) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(fileInputStream));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }
    }


}