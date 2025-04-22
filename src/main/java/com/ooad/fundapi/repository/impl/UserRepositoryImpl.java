package com.ooad.fundapi.repository.impl;

import com.ooad.fundapi.model.User;
import com.ooad.fundapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.user_id = rs.getInt("user_id");
            user.user_name = rs.getString("user_name");
            user.password_hash = rs.getString("password_hash");
            user.salt = rs.getString("salt");
            return user;
        }
    };

    @Override
    public int insertUser(String username, String passwordHash, String salt) {
        String insertSql = "INSERT INTO user (user_name, password_hash, salt) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, username, passwordHash, salt);

        String selectSql = "SELECT user_id FROM user WHERE user_name = ?";
        return jdbcTemplate.queryForObject(selectSql, Integer.class, username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT user_id, user_name, password_hash, salt FROM user WHERE user_name = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
