package com.batch.springboot.csvtomysql.config;

import org.springframework.batch.item.ItemProcessor;
import com.batch.springboot.csvtomysql.model.User;

public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        // Example: Convert the user's name to uppercase
//        String name = user.getFirstName();
        user.setFirstName("raj");

        // You can add more processing logic here as needed

        return user;
    }
}
