package com.ooad.fundapi.controller;

import com.ooad.fundapi.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/list")
    public ResponseEntity<?> getWatchlist(@RequestBody Map<String, Object> payload) {
        return watchlistService.listWatchlist(payload);
    }

    @PostMapping("/addone")
    public ResponseEntity<?> addWatchlistItem(@RequestBody Map<String, Object> payload) {
        return watchlistService.addOne(payload);
    }

    @PostMapping("/addmany")
    public ResponseEntity<?> addMultipleItems(@RequestBody Map<String, Object> payload) {
        return watchlistService.addMany(payload);
    }

    @PostMapping("/deleteone")
    public ResponseEntity<?> deleteOneItem(@RequestBody Map<String, Object> payload) {
        return watchlistService.deleteOne(payload);
    }
}
