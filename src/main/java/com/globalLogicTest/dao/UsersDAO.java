package com.globalLogicTest.dao;

import org.springframework.stereotype.Repository;

import com.globalLogicTest.model.User;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Repository
@Configurable
public interface UsersDAO extends CrudRepository<User, Long>{
    Optional<User> findByEmail(String email);
}

