package com.ooad.fundapi.service;

import com.ooad.fundapi.repository.PortfolioRepository;
import com.ooad.fundapi.utils.HttpErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    /**
     * Add a portfolio item
     */
    public ResponseEntity<?> addPortfolioItem(Map<String, Object> payload) {
        // Validate required fields
        if (!payload.containsKey("user_id") || !payload.containsKey("fund_id") ||
                !payload.containsKey("bought_on") || !payload.containsKey("bought_for") ||
                !payload.containsKey("invested_amount")) {
            return new ResponseEntity<>(
                    Map.of("error", "User ID, Fund ID, Bought On Date, Bought For amount and Amount invested required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        // Extract values from payload
        Integer userId = Integer.valueOf(payload.get("user_id").toString());
        Integer fundId = Integer.valueOf(payload.get("fund_id").toString());
        String boughtOn = payload.get("bought_on").toString();
        Double boughtFor = Double.valueOf(payload.get("bought_for").toString());
        Double investedAmount = Double.valueOf(payload.get("invested_amount").toString());

        // Optional fields
        String soldOn = payload.containsKey("sold_on") && payload.get("sold_on") != null ?
                payload.get("sold_on").toString() : null;
        Double soldFor = payload.containsKey("sold_for") && payload.get("sold_for") != null ?
                Double.valueOf(payload.get("sold_for").toString()) : null;
        Double returnAmount = payload.containsKey("return_amount") && payload.get("return_amount") != null ?
                Double.valueOf(payload.get("return_amount").toString()) : null;
        try {
            portfolioRepository.addPortfolioItem(userId, fundId, boughtOn, boughtFor, investedAmount, soldOn, soldFor, returnAmount);

            return new ResponseEntity<>(
                    Map.of("message", "Added to portfolio"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_SUCCESS_NEW)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error adding to portfolio"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INTERNAL_ALL)
            );
        }
    }

    /**
     * Update sell information in portfolio
     */
    public ResponseEntity<?> updatePortfolioItem(Map<String, Object> payload) {
        // Validate required fields
        if (!payload.containsKey("user_id") || !payload.containsKey("fund_id") ||
                !payload.containsKey("bought_on")) {
            return new ResponseEntity<>(
                    Map.of("error", "User ID, Fund ID and Bought On Date required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        // Extract values from payload
        Integer userId = Integer.valueOf(payload.get("user_id").toString());
        Integer fundId = Integer.valueOf(payload.get("fund_id").toString());
        String boughtOn = payload.get("bought_on").toString();

        // Optional fields
        String soldOn = payload.containsKey("sold_on") && payload.get("sold_on") != null ?
                payload.get("sold_on").toString() : null;
        Double soldFor = payload.containsKey("sold_for") && payload.get("sold_for") != null ?
                Double.valueOf(payload.get("sold_for").toString()) : null;
        Double returnAmount = payload.containsKey("return_amount") && payload.get("return_amount") != null ?
                Double.valueOf(payload.get("return_amount").toString()) : null;

        try {
            portfolioRepository.updatePortfolioItem(userId, fundId, boughtOn, soldOn, soldFor, returnAmount);

            return new ResponseEntity<>(
                    Map.of("message", "Updated portfolio"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_SUCCESS_NEW)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error updating portfolio"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INTERNAL_ALL)
            );
        }
    }

    /**
     * List all portfolio items of user
     */
    public ResponseEntity<?> listPortfolio(Map<String, Object> payload) {
        if (!payload.containsKey("user_id")) {
            return new ResponseEntity<>(
                    Map.of("error", "User ID required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        Integer userId = Integer.valueOf(payload.get("user_id").toString());
        Map<String, Object> response = new HashMap<>();

        try {
            List<List<Object>> results = portfolioRepository.getPortfolioByUserId(userId);
            response.put("results", results);
            return new ResponseEntity<>(response, HttpStatus.valueOf(HttpErrorCodes.ERR_SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("message", "Error fetching portfolio"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INTERNAL_ALL)
            );
        }
    }

    /**
     * Delete an item from portfolio
     */
    public ResponseEntity<?> deletePortfolioItem(Map<String, Object> payload) {
        if (!payload.containsKey("user_id") || !payload.containsKey("fund_id") ||
                !payload.containsKey("bought_on")) {
            return new ResponseEntity<>(
                    Map.of("error", "User ID, Fund ID and bought on date required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        Integer userId = Integer.valueOf(payload.get("user_id").toString());
        Integer fundId = Integer.valueOf(payload.get("fund_id").toString());
        String boughtOn = payload.get("bought_on").toString();

        try {
            portfolioRepository.deletePortfolioItem(userId, fundId, boughtOn);

            return new ResponseEntity<>(
                    Map.of("message", "Record dropped successfully"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_SUCCESS)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Failed to process query"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INTERNAL_ALL)
            );
        }
    }
}