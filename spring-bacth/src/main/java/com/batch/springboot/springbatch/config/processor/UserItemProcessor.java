package com.batch.springboot.springbatch.config.processor;

import org.springframework.batch.item.ItemProcessor;
import com.batch.springboot.springbatch.model.User;

public class UserItemProcessor implements ItemProcessor<User, User> {
    @Override
    public User process(User user) {
        user.setFirstName(user.getEmail().toLowerCase());
        return user;
    }
}
