package com.ooad.fundapi.controller;

import com.ooad.fundapi.dto.AuthRequest;
import com.ooad.fundapi.dto.AuthResponse;
import com.ooad.fundapi.dto.RegisterRequest;
import com.ooad.fundapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ooad.fundapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RegisterLoginController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterLoginController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Register request: " + request.username + " " + request.password);
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.verifyUser(request);
    }
}