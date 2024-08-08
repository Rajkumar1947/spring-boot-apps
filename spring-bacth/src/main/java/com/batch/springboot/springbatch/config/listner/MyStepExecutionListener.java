package com.batch.springboot.springbatch.config.listner;

import com.batch.springboot.springbatch.config.writer.SynchronizedExcelItemWriter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class MyStepExecutionListener implements StepExecutionListener {

    @Autowired
    private SynchronizedExcelItemWriter stepScopedItemWriter;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // This is called before the step starts
        System.out.println("StepScopedItemWriter Parameter: " );
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // This is called after the step ends
        try {
            stepScopedItemWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ExitStatus.COMPLETED;
    }
}
