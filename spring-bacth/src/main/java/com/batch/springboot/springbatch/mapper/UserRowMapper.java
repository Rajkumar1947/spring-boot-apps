package com.batch.springboot.springbatch.mapper;

import com.batch.springboot.springbatch.model.UserDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDTO> {
    @Override
    public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDTO user = new UserDTO();
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        return user;
    }
}
