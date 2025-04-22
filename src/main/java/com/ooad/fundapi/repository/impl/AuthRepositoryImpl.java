package com.ooad.fundapi.repository.impl;

import com.ooad.fundapi.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void invalidateToken(int userId) {
        String deleteSql = "DELETE FROM auth WHERE user_id = ?";
        jdbcTemplate.update(deleteSql, userId);
    }

    @Override
    public void storeToken(int userId, String token, Date createdOn) {
        String insertSql = "INSERT INTO auth (user_id, token_hash, created_on) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, userId, token, createdOn);
    }
}
