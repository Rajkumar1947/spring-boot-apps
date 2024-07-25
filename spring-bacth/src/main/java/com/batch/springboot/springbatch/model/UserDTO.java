package com.batch.springboot.springbatch.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;

}
