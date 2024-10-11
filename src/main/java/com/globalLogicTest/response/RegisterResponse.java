package com.globalLogicTest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Long id;

    private Date created;

    private Date lastLogin;

    private String token;

    private Boolean isActive;

}