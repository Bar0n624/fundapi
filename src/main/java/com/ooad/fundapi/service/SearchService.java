package com.ooad.fundapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<?> searchFundByName(String query) {
        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Empty search query"));
        }

        String formatted = "%" + String.join("%", query.trim().split("\\s+")) + "%";

        try {
            List<List<Object>> results = jdbcTemplate.query(
                    "SELECT DISTINCT fund_name.fund_id, fund_name.fund_name, fund.one_year " +
                            "FROM fund_name JOIN fund ON fund_name.fund_id = fund.fund_id " +
                            "WHERE fund_name.fund_name LIKE ? ORDER BY fund.fund_rank LIMIT 15",
                    (rs, rowNum) -> List.of(rs.getInt(1), rs.getString(2), rs.getDouble(3)),
                    formatted);
            return ResponseEntity.ok(Map.of("results", results));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }
    }

    public ResponseEntity<?> searchByCompany(String companyId) {
        if (companyId == null || companyId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Empty search query"));
        }

        try {
            Map<String, Object> res = new HashMap<>();
            Map<String, Object> company = jdbcTemplate.queryForMap("SELECT company_name FROM fund_company WHERE company_id = ?", companyId);
            res.put("company_name", company.get("company_name"));

            List<List<Object>> results = jdbcTemplate.query(
                    "SELECT DISTINCT fund_name.fund_id AS fid, fund_name.fund_name AS fname, ROUND(fund.one_year, 2) AS one_year " +
                            "FROM fund_name JOIN fund_company ON fund_name.company_id = fund_company.company_id " +
                            "JOIN fund ON fund_name.fund_id = fund.fund_id WHERE fund_company.company_id = ? ORDER BY fund.fund_rank",
                    (rs, rowNum) -> List.of(rs.getInt("fid"), rs.getString("fname"), rs.getDouble("one_year")),
                    companyId);
            res.put("results", results);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }
    }

    public ResponseEntity<?> searchByCategory(String categoryId) {
        if (categoryId == null || categoryId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Empty search query"));
        }

        try {
            Map<String, Object> res = new HashMap<>();
            Map<String, Object> category = jdbcTemplate.queryForMap("SELECT category_name FROM fund_category WHERE category_id = ?", categoryId);
            res.put("category_name", category.get("category_name"));

            List<List<Object>> results = jdbcTemplate.query(
                    "SELECT DISTINCT fund_name.fund_id AS fid, fund_name.fund_name AS fname, ROUND(fund.one_year, 2) AS one_year " +
                            "FROM fund_name JOIN fund_category ON fund_name.category_id = fund_category.category_id " +
                            "JOIN fund ON fund_name.fund_id = fund.fund_id WHERE fund_category.category_id = ? ORDER BY fund.fund_category_rank",
                    (rs, rowNum) -> List.of(rs.getInt("fid"), rs.getString("fname"), rs.getDouble("one_year")),
                    categoryId);
            res.put("results", results);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }
    }

    public ResponseEntity<?> getAllFunds() {
        try {
            List<List<Object>> results = jdbcTemplate.query(
                    "SELECT DISTINCT fund_id AS f_id, fund_name as f_name FROM fund_name",
                    (rs, rowNum) -> List.of(rs.getInt("f_id"), rs.getString("f_name"))
            );
            return ResponseEntity.ok(Map.of("results", results));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }
    }

    public ResponseEntity<?> getAllCompanies() {
        try {
            List<List<Object>> results = jdbcTemplate.query(
                    "SELECT DISTINCT company_id AS c_id, company_name as c_name FROM fund_company",
                    (rs, rowNum) -> List.of(rs.getInt("c_id"), rs.getString("c_name"))
            );
            return ResponseEntity.ok(Map.of("results", results));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }
    }

    public ResponseEntity<?> getAllCategories() {
        try {
            List<List<Object>> results = jdbcTemplate.query(
                    "SELECT DISTINCT category_id AS c_id, category_name as c_name FROM fund_category",
                    (rs, rowNum) -> List.of(rs.getInt("c_id"), rs.getString("c_name"))
            );
            return ResponseEntity.ok(Map.of("results", results));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }
    }
}
