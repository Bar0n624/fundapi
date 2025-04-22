package com.ooad.fundapi.controller;

import com.ooad.fundapi.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/add")
    public ResponseEntity<?> addPortfolioItem(@RequestBody Map<String, Object> payload) {
        return portfolioService.addPortfolioItem(payload);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePortfolioItem(@RequestBody Map<String, Object> payload) {
        return portfolioService.updatePortfolioItem(payload);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getPortfolio(@RequestBody Map<String, Object> payload) {
        return portfolioService.listPortfolio(payload);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deletePortfolioItem(@RequestBody Map<String, Object> payload) {
        return portfolioService.deletePortfolioItem(payload);
    }
}