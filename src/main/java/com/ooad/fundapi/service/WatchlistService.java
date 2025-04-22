package com.ooad.fundapi.service;

import com.ooad.fundapi.repository.WatchlistRepository;
import com.ooad.fundapi.utils.HttpErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    /**
     * Lists all watchlist items for a user
     */
    public ResponseEntity<?> listWatchlist(Map<String, Object> payload) {
        if (!payload.containsKey("user_id")) {
            return new ResponseEntity<>(
                    Map.of("error", "User ID is required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        Integer userId = Integer.valueOf(payload.get("user_id").toString());
        Map<String, Object> response = new HashMap<>();

        try {
            List<List<Object>> results = watchlistRepository.getWatchlistByUserId(userId);
            response.put("results", results);
            return new ResponseEntity<>(response, HttpStatus.valueOf(HttpErrorCodes.ERR_SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error fetching watchlist"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INTERNAL_ALL)
            );
        }
    }

    /**
     * Adds a single item to watchlist
     */
    public ResponseEntity<?> addOne(Map<String, Object> payload) {
        if (!payload.containsKey("user_id") || !payload.containsKey("fund_id")) {
            return new ResponseEntity<>(
                    Map.of("error", "User ID and Fund ID required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        Integer userId = Integer.valueOf(payload.get("user_id").toString());
        Integer fundId = Integer.valueOf(payload.get("fund_id").toString());

        try {
            watchlistRepository.addWatchlistItem(userId, fundId);
            return new ResponseEntity<>(
                    Map.of("message", "Added to watchlist"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_SUCCESS_NEW)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error adding to watchlist"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }
    }

    /**
     * Adds multiple items to watchlist
     */
    public ResponseEntity<?> addMany(Map<String, Object> payload) {
        if (!payload.containsKey("items") || !(payload.get("items") instanceof List)) {
            return new ResponseEntity<>(
                    Map.of("error", "Items list is required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");

        // Validate each item has user_id and fund_id
        for (Map<String, Object> item : items) {
            if (!item.containsKey("user_id") || !item.containsKey("fund_id")) {
                return new ResponseEntity<>(
                        Map.of("error", "Each item must contain User ID and Fund ID"),
                        HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
                );
            }
        }

        try {
            // Create a batch update for better performance
            List<Object[]> batchArgs = new ArrayList<>();
            for (Map<String, Object> item : items) {
                Integer userId = Integer.valueOf(item.get("user_id").toString());
                Integer fundId = Integer.valueOf(item.get("fund_id").toString());
                batchArgs.add(new Object[]{userId, fundId});
            }

            watchlistRepository.addMultipleWatchlistItems(batchArgs);

            return new ResponseEntity<>(
                    Map.of("message", "Added multiple items to watchlist"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_SUCCESS_NEW)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error adding items to watchlist"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }
    }

    /**
     * Deletes a single item from watchlist
     */
    public ResponseEntity<?> deleteOne(Map<String, Object> payload) {
        if (!payload.containsKey("user_id") || !payload.containsKey("fund_id")) {
            return new ResponseEntity<>(
                    Map.of("error", "User ID and Fund ID required"),
                    HttpStatus.valueOf(HttpErrorCodes.ERR_INVALID)
            );
        }

        Integer userId = Integer.valueOf(payload.get("user_id").toString());
        Integer fundId = Integer.valueOf(payload.get("fund_id").toString());

        try {
            watchlistRepository.deleteWatchlistItem(userId, fundId);

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