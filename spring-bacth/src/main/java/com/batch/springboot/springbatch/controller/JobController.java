package com.batch.springboot.springbatch.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job exportUser;

    @Autowired
    private Job importUsers;


    @PostMapping("/importCustomers")
    public ResponseEntity<?> importCsvToDBJob() {
        return runJob(importUsers, null);
    }


    private ResponseEntity<?> runJob(Job job, String lastName) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobName", job.getName())
                    .addString("date", LocalDateTime.now().toString())
                    .addString("lastName", lastName)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            while (jobExecution.isRunning()) {
                Thread.sleep(1000);
            }
            if(jobExecution.getStatus().equals("COMPLETED")) {
                System.out.println("Job Execution completed:");
            }else {
                System.out.println(jobExecution.getStatus());
            }
            return ResponseEntity.ok("Batch Job " + job.getName()
                    + " started with JobExecutionId: " + jobExecution.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start Batch Job: " + e.getMessage());
        }
    }

    @PostMapping("/exportUser/{lastName}")
    public ResponseEntity<?> exportUser(@PathVariable String lastName) throws Exception {
      runJob(exportUser,lastName);
      return download();
    }

public ResponseEntity<?> download() {
        // Adjust the file path based on the lastName parameter if needed
        File file = new File("output.xlsx");
        System.out.println("Download called::::");
        if (file.exists()) {
            try {
                // Open the file input stream
                FileInputStream fileInputStream = new FileInputStream(file);

                // Create the HttpHeaders
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

                // Return the ResponseEntity with the InputStreamResource
                ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                        .headers(headers)
                        .body(new InputStreamResource(fileInputStream));

                // Return the response entity
                return responseEntity;
            } catch (IOException e) {
                // Handle the exception
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while processing the file: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }
    }
}