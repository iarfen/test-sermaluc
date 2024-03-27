package com.sermalucTest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sermalucTest.dao.UsersDAO;
import com.sermalucTest.dto.UserDTO;
import com.sermalucTest.model.Phone;
import com.sermalucTest.model.User;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class UsersController {
    
    @Autowired
    private UsersDAO usersDAO;

    @GetMapping("/users")
    public List<User> getusers() {
        return (List<User>) usersDAO.findAll();
    }
    
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) throws ResponseStatusException {
        return usersDAO.findById(userId).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"User not found\"}"); } );
    }

    @PostMapping("/users")
    public User addUser(@RequestBody UserDTO userDTO) throws JsonProcessingException {
        if (Objects.isNull(userDTO.getName()) || userDTO.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Nombre requerido\"}");
        }
        if (Objects.isNull(userDTO.getEmail()) || userDTO.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email requerido\"}");
        }
        if (userDTO.getEmail().matches("[A-Za-z0-9.-]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email no sigue el formato correcto\"}");
        }
        List<User> users = (List<User>) usersDAO.findAll();
        for (User userDb : users) {
            if (userDTO.getEmail().equals(userDb.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"El correo ya está registrado\"}");
            }
        }
        if (Objects.isNull(userDTO.getPassword()) || userDTO.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña requerida\"}");
        }
        if (userDTO.getEmail().matches("[A-Za-z0-9]+[0-9][0-9]")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña no sigue el formato correcto\"}");
        }
        if (Objects.isNull(userDTO.getPhones()) || userDTO.getPhones().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Teléfonos requeridos\"}");
        }
        int i = 1;
        for (Phone phone : userDTO.getPhones()) {
            if (Objects.isNull(phone.getNumber()) || phone.getNumber() != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Número de teléfono " + Integer.toString(i) + " requerido\"}");
            }
            if (Objects.isNull(phone.getCityCode()) || phone.getCityCode() != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Código de ciudad de teléfono " + Integer.toString(i) + " requerido\"}");
            }
            if (Objects.isNull(phone.getCountryCode()) || phone.getCountryCode() != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Código de país de teléfono " + Integer.toString(i) + " requerido\"}");
            }
            i++;
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhones(userDTO.getPhones());
        Date createdAt = new Date();
        user.setCreatedAt(createdAt);
        user.setModifiedAt(createdAt);
        user.setLastLogin(createdAt);
        user.setIsActive(true);
        SecretKey key = Jwts.SIG.HS256.key().build();
        String jws = Jwts.builder().subject("sermalucTest").claim("name",userDTO.getName()).signWith(key).compact();
        user.setToken(jws);
        usersDAO.save(user);
        return user;
    }
    
    @PutMapping("/users/{userId}")
    public User putUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) throws JsonProcessingException, ResponseStatusException {
        Optional<User> dbUser = usersDAO.findById(userId);
        if (dbUser.isPresent())
        {
            if (Objects.isNull(userDTO.getName()) || userDTO.getName().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Nombre requerido\"}");
            }
            if (Objects.isNull(userDTO.getEmail()) || userDTO.getEmail().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email requerido\"}");
            }
            if (userDTO.getEmail().matches("[A-Za-z0-9.-]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email no sigue el formato correcto\"}");
            }
            List<User> users = (List<User>) usersDAO.findAll();
            for (User userDb : users) {
                if (userDTO.getEmail().equals(userDb.getEmail()) && !userDb.getEmail().equals(dbUser.get().getEmail())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"El correo ya está registrado\"}");
                }
            }
            if (Objects.isNull(userDTO.getPassword()) || userDTO.getPassword().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña requerida\"}");
            }
            if (userDTO.getEmail().matches("[A-Za-z0-9]+[0-9][0-9]")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña no sigue el formato correcto\"}");
            }
            if (Objects.isNull(userDTO.getPhones()) || userDTO.getPhones().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Teléfonos requeridos\"}");
            }
            int i = 1;
            for (Phone phone : userDTO.getPhones()) {
                if (Objects.isNull(phone.getNumber()) || phone.getNumber() != 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Número de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                if (Objects.isNull(phone.getCityCode()) || phone.getCityCode() != 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Código de ciudad de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                if (Objects.isNull(phone.getCountryCode()) || phone.getCountryCode() != 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mensaje\": \"Código de país de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                i++;
            }
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setPhones(userDTO.getPhones());
            user.setId(userId);
            user.setCreatedAt(dbUser.get().getCreatedAt());
            Date modifiedAt = new Date();
            user.setModifiedAt(modifiedAt);
            user.setLastLogin(dbUser.get().getLastLogin());
            user.setIsActive(dbUser.get().getIsActive());
            user.setToken(dbUser.get().getToken());
            usersDAO.save(user);
            return user;
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"User not found\"}");
        }
    }
    
    @DeleteMapping("/users/{userId}")
    void deleteUser(@PathVariable Long userId) throws ResponseStatusException {
        Optional<User> dbUser = usersDAO.findById(userId);
        if (dbUser.isPresent())
        {
            usersDAO.deleteById(userId);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"User not found\"}");
        }
    }
}
