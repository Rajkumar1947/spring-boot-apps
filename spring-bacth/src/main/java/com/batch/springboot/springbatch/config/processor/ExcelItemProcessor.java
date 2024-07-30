package com.batch.springboot.springbatch.config.processor;

import com.batch.springboot.springbatch.model.UserDTO;
import com.batch.springboot.springbatch.service.TestService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class ExcelItemProcessor implements ItemProcessor<UserDTO, UserDTO> {

    @Autowired
    TestService service;
    @Override
    public UserDTO process(UserDTO userDTO) throws Exception {
        service.test("mess");
        System.out.println("CAlled" );
        return userDTO;
    }
}
