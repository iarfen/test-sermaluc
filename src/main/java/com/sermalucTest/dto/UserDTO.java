package com.sermalucTest.dto;

import com.sermalucTest.model.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    public String name;

    public String email;

    public String password;

    public List<Phone> phones;
}
