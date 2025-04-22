package com.ooad.fundapi.controller;

import com.ooad.fundapi.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search/fund")
    public ResponseEntity<?> searchFund(@RequestParam("q") String query) {
        return searchService.searchFundByName(query);
    }

    @GetMapping("/search/company")
    public ResponseEntity<?> searchCompany(@RequestParam("c_id") String companyId) {
        return searchService.searchByCompany(companyId);
    }

    @GetMapping("/search/category")
    public ResponseEntity<?> searchCategory(@RequestParam("c_id") String categoryId) {
        return searchService.searchByCategory(categoryId);
    }

    @GetMapping("/all/fund")
    public ResponseEntity<?> getAllFunds() {
        return searchService.getAllFunds();
    }

    @GetMapping("/all/company")
    public ResponseEntity<?> getAllCompanies() {
        return searchService.getAllCompanies();
    }

    @GetMapping("/all/category")
    public ResponseEntity<?> getAllCategories() {
        return searchService.getAllCategories();
    }
}
