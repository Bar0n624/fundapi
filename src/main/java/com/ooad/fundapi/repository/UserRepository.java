package com.ooad.fundapi.repository;

import com.ooad.fundapi.model.User;

import java.util.Optional;

public interface UserRepository {
    int insertUser(String username, String passwordHash, String salt);
    Optional<User> findByUsername(String username);
}
