package com.ooad.fundapi.controller;

import com.ooad.fundapi.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class HomeFundController {

    @Autowired
    private FundService fundService;

    @GetMapping("/home")
    public ResponseEntity<?> getHomeData(@RequestParam("u_id") String userId) {
        return fundService.loadHome(userId);
    }

    @GetMapping("/fund")
    public ResponseEntity<?> getFundInfo(@RequestParam("f_id") String fundId) {
        return fundService.loadFund(fundId);
    }

    @GetMapping("/fund/graph_data")
    public ResponseEntity<?> getFundGraphData(@RequestParam("f_id") String fundId) {
        return fundService.loadGraphData(fundId);
    }
}
