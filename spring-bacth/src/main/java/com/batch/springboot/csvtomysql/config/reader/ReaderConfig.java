package com.batch.springboot.csvtomysql.config.reader;

import javax.sql.DataSource;

import com.batch.springboot.csvtomysql.config.UserRowMapper;
import com.batch.springboot.csvtomysql.model.UserDTO;
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
        queryProvider.setSelectClause("select customer_id, first_name, email");
        queryProvider.setFromClause("from CUSTOMERS_INFO");
        queryProvider.setWhereClause("where first_name = :lastName");
        queryProvider.setSortKey("customer_id");
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

