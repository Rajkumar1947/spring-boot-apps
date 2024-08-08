package com.batch.springboot.springbatch.config.listner;

//package com.batch.springboot.springbatch.config;

import com.batch.springboot.springbatch.config.writer.SynchronizedExcelItemWriter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobCompletionListener implements JobExecutionListener {

    private SynchronizedExcelItemWriter writer;

    public void setWriter(SynchronizedExcelItemWriter writer) {
        this.writer = writer;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Nothing to do before the job
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
       System.out.println("Job completed:");
    }
}

