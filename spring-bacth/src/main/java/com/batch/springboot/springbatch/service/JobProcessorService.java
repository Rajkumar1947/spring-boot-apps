package com.batch.springboot.springbatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobProcessorService {

    private final JobLauncher jobLauncher;
    public ResponseEntity<?> runJob(Job job, String lastName) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobName", job.getName())
                    .addString("outputFileName", "output-"+ lastName+".xlsx")
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
}
