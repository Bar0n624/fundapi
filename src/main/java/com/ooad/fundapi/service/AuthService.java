package com.ooad.fundapi.service;

import com.ooad.fundapi.dto.AuthRequest;
import com.ooad.fundapi.dto.AuthResponse;
import com.ooad.fundapi.dto.RegisterRequest;
import com.ooad.fundapi.model.User;
import com.ooad.fundapi.repository.AuthRepository;
import com.ooad.fundapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.security.MessageDigest;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthRepository authRepo;

    public ResponseEntity<?> registerUser(RegisterRequest request) {
        final Logger logger = LoggerFactory.getLogger(AuthService.class);
        if (request.username == null || request.password == null) {
            logger.info(request.password);
            return ResponseEntity.badRequest().body("{\"error\": \"Username and password required\"}");
        }

        try {
            String salt = generateSalt();
            String hash = sha256(request.password + salt);

            int userId = userRepo.insertUser(request.username, hash, salt);
            return ResponseEntity.ok("{\"message\": \"User registered successfully\", \"user_id\": " + userId + "}");

        } catch (Exception e) {
            logger.error("Error registering user: " + e.getMessage());
            return ResponseEntity.badRequest().body("{\"error\": \"Error registering user\"}");
        }
    }

    public ResponseEntity<?> verifyUser(AuthRequest request) {
        Optional<User> user = userRepo.findByUsername(request.username);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
        }

        String computedHash = sha256(request.password + user.get().salt);
        if (!computedHash.equals(user.get().password_hash)) {
            return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
        }

        String token = generateToken(user.get().user_id);
        authRepo.invalidateToken(user.get().user_id);
        authRepo.storeToken(user.get().user_id, token, Date.valueOf(LocalDate.now()));

        AuthResponse response = new AuthResponse();
        response.message = "Login successful";
        response.user_id = user.get().user_id;
        response.auth_token = token;

        return ResponseEntity.ok(response);
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return bytesToHex(salt);
    }

    private String generateToken(int userId) {
        return sha256(System.currentTimeMillis() + "" + userId + "bar0n&vb");
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes)
            hex.append(String.format("%02x", b));
        return hex.toString();
    }
}
