package com.batch.springboot.csvtomysql.config;

import com.batch.springboot.csvtomysql.model.UserDTO;
import org.springframework.batch.item.ItemProcessor;

public class UserItemWriteProcessor implements ItemProcessor<UserDTO, UserDTO> {

    @Override
    public UserDTO process(UserDTO user) throws Exception {
        return user;
    }
}
