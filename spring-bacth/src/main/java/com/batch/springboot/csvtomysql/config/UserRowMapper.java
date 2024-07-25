package com.batch.springboot.csvtomysql.config;

import com.batch.springboot.csvtomysql.model.User;
import com.batch.springboot.csvtomysql.model.UserDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDTO> {
    @Override
    public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDTO user = new UserDTO();
        System.out.println("Call came to mapper");
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        System.out.println("Mapper output" + user);
        return user;
    }
}
