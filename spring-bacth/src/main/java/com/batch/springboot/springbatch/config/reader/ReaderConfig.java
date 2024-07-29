package com.batch.springboot.springbatch.config.reader;

import javax.sql.DataSource;

import com.batch.springboot.springbatch.mapper.UserRowMapper;
import com.batch.springboot.springbatch.model.UserDTO;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class ReaderConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    @StepScope
    public JdbcPagingItemReader<UserDTO> databaseReader(@Value("#{jobParameters['lastName']}") String lastName) throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        System.out.println("Last name:: " +lastName);
        queryProvider.setSelectClause("select id, first_name, email");
        queryProvider.setFromClause("from USER_INFO");
        queryProvider.setWhereClause("where first_name = :lastName");
        queryProvider.setSortKey("email");
        return new JdbcPagingItemReaderBuilder<UserDTO>()
                .name("databaseReader")
                .dataSource(dataSource)
                .fetchSize(100)
                .queryProvider(queryProvider.getObject())
                .parameterValues(Collections.singletonMap("lastName", lastName))
                .rowMapper(new UserRowMapper())
                .build();
    }
}

