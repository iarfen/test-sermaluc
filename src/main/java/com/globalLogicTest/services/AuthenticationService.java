package com.globalLogicTest.services;

import com.globalLogicTest.dto.RegisterUserDTO;
import com.globalLogicTest.model.Phone;
import com.globalLogicTest.model.User;
import com.globalLogicTest.dao.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {
    @Autowired
    private UsersDAO userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User signup(RegisterUserDTO input) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        User user = new User();
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        List<Phone> phones = new ArrayList<>();
        for (RegisterUserDTO.RegisterPhoneDTO registerPhone : input.getPhones())
        {
            Phone phone = new Phone();
            phone.setNumber(registerPhone.getNumber());
            phone.setCitycode(registerPhone.getCitycode());
            phone.setCountrycode(registerPhone.getCountrycode());
            phones.add(phone);
        }

        user.setPhones(phones);
        user.setLastLogin(new Date());
        user.setIsActive(true);

        return userRepository.save(user);
    }

    public User login(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
}