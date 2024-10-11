package com.globalLogicTest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RegisterUserDTO {

    private String name;

    private String email;

    private String password;

    private List<RegisterPhoneDTO> phones;

    @Data
    @AllArgsConstructor
    public static class RegisterPhoneDTO {

        private Long number;

        private Long citycode;

        private Long countrycode;
    }

}