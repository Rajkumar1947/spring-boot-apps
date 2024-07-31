package com.batch.springboot.springbatch.config;

import com.batch.springboot.springbatch.config.processor.ExcelItemProcessor;
import com.batch.springboot.springbatch.config.processor.UserItemProcessor;
import com.batch.springboot.springbatch.config.reader.ReaderConfig;
import com.batch.springboot.springbatch.config.writer.MyStepExecutionListener;
import com.batch.springboot.springbatch.model.User;
import com.batch.springboot.springbatch.model.UserDTO;
import com.batch.springboot.springbatch.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private UserRepository repository;
//    private ExcelWriter excelWriter;
//    private TaskExecutor taskExecutor;
    private ReaderConfig readerConfig;
    private SynchronizedExcelItemWriter stepScopedItemWriter;


    @Bean
    public FlatFileItemReader<User> reader() {
        FlatFileItemReader<User> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<User> lineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<User> writer() {
        RepositoryItemWriter<User> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<User, User>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job importUsers() {
        return jobBuilderFactory.get("importUsers")
                .flow(step1()).end().build();

    }

    // Read data from database and write in excel
    @Bean
    public Step writeExcelFile() throws Exception {
        return stepBuilderFactory.get("writeExcelFile")
                .<UserDTO, UserDTO>chunk(100)
                .reader(readerConfig.databaseReader(""))
                .processor(excelFileProcessor())
                .writer(stepScopedItemWriter)
                .taskExecutor(taskExecutor())  // Enable multi-threaded processing
                .throttleLimit(10)
                // Set the maximum number of concurrent threads
                .listener(myStepExecutionListener())
                .build();
    }
    @Bean
    public StepExecutionListener myStepExecutionListener() {
        return new MyStepExecutionListener();
    }
    @Bean
    public ExcelItemProcessor excelFileProcessor() {
        return new ExcelItemProcessor();
    }

    @Bean
    public JobCompletionListener jobCompletionListener() {
        return new JobCompletionListener();
    }
    @Bean
    public Job exportUser() throws Exception {
        return jobBuilderFactory.get("exportUser")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener())
                .flow(writeExcelFile())
                .end().build();
    }
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("batch-thread-");
        executor.initialize();
        return executor;
    }

}