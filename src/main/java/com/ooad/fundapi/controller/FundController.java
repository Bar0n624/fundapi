package com.ooad.fundapi.controller;

import com.ooad.fundapi.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class FundController {

    private final FundRepository fundRepository;

    @Autowired
    public FundController(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    /**
     * Get the price of a fund on a given date or the nearest previous date
     *
     * @param fundId Fund ID
     * @param date Requested date
     * @return Price on that date or nearest previous date
     */
    @GetMapping("/fund/date")
    public ResponseEntity<?> getFundPriceOnDate(
            @RequestParam("f_id") String fundId,
            @RequestParam("date") String date) {

        if (fundId == null || date == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Fund ID and Date required"));
        }

        try {
            Map<String, Object> result = fundRepository.getFundPriceOnDate(fundId, date);
            if (result.isEmpty()) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not process query"));
        }
    }

    /**
     * Get top performing funds
     *
     * @return List of top funds
     */
    @GetMapping("/top/fund")
    public ResponseEntity<?> getTopFunds() {
        try {
            Map<String, Object> result = fundRepository.getTopFunds();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not process query"));
        }
    }
}