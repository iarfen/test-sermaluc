package com.sermalucTest.dao;

import org.springframework.stereotype.Repository;

import com.sermalucTest.model.User;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.repository.CrudRepository;

@Repository
@Configurable
public interface UsersDAO extends CrudRepository<User, Long>{}

