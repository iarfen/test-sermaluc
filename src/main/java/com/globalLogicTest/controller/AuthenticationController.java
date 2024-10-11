package com.globalLogicTest.controller;

import com.globalLogicTest.model.Phone;
import com.globalLogicTest.model.User;
import com.globalLogicTest.dto.RegisterUserDTO;
import com.globalLogicTest.response.LoginResponse;
import com.globalLogicTest.response.RegisterResponse;
import com.globalLogicTest.services.AuthenticationService;
import com.globalLogicTest.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterUserDTO registerUserDto) throws Exception {
        if (!Pattern.compile("[A-Za-z0-9.-]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+")
                .matcher(registerUserDto.getEmail())
                .matches()) {
            throw new Exception("{\"error\": [{\"timestamp\": \"" + new Date() + "\"," +
                    "\"codigo\": 1," +
                    "\"detail\": \"Invalid email address\"}]}");
        }
        if (registerUserDto.getPassword().length() < 8)
        {
            throw new Exception("{\"error\": [{\"timestamp\": \"" + new Date() + "\"," +
                    "\"codigo\": 2," +
                    "\"detail\": \"Password length is lower than 8 characters\"}]}");
        }
        if (registerUserDto.getPassword().length() > 12)
        {
            throw new Exception("{\"error\": [{\"timestamp\": \"" + new Date() + "\"," +
                    "\"codigo\": 3," +
                    "\"detail\": \"Password length is greather than 12 characters\"}]}");
        }
        int numberDigits = 0;
        int numberUpper = 0;
        for(int i = 0; i < registerUserDto.getPassword().length(); i++) {
            char currentCharacter = registerUserDto.getPassword().charAt(i);
            if( Character.isDigit(currentCharacter)) {
                numberDigits++;
            }
            else if (Character.isUpperCase(currentCharacter)) {
                numberUpper++;
            }
        }
        if (numberUpper != 1)
        {
            throw new Exception("{\"error\": [{\"timestamp\": \"" + new Date() + "\"," +
                    "\"codigo\": 4," +
                    "\"detail\": \"Password must have exactly one uppercase character\"}]}");
        }
        if (numberDigits != 2)
        {
            throw new Exception("{\"error\": [{\"timestamp\": \"" + new Date() + "\"," +
                    "\"codigo\": 5," +
                    "\"detail\": \"Password must have exactly two characters of digits\"}]}");
        }
        User registeredUser = authenticationService.signup(registerUserDto);
        String jwtToken = jwtService.generateToken(registeredUser);
        RegisterResponse registerResponse = new RegisterResponse(registeredUser.getId(),
                registeredUser.getCreatedAt(),
                registeredUser.getLastLogin(),
                jwtToken,
                registeredUser.getIsActive());

        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestHeader(name="Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        User authenticatedUser = authenticationService.login(email);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        List<RegisterUserDTO.RegisterPhoneDTO> responsePhones = new ArrayList<>();
        for(Phone phone : authenticatedUser.getPhones())
        {
            RegisterUserDTO.RegisterPhoneDTO responsePhone = new RegisterUserDTO.RegisterPhoneDTO(phone.getNumber(),phone.getCitycode(),phone.getCountrycode());
        }

        LoginResponse loginResponse = new LoginResponse(authenticatedUser.getId(),
                authenticatedUser.getCreatedAt(),
                authenticatedUser.getLastLogin(),
                jwtToken,
                authenticatedUser.getIsActive(),
                authenticatedUser.getName(),
                authenticatedUser.getEmail(),
                authenticatedUser.getPassword(),
                responsePhones);

        return ResponseEntity.ok(loginResponse);
    }
}
