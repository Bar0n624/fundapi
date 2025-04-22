package com.ooad.fundapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class FundService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<?> loadHome(String userId) {
        if (userId == null || !userId.matches("\\d+")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valid User ID required"));
        }

        Map<String, List<List<Object>>> res = new HashMap<>();

        try {
            res.put("one_year", queryTopFunds("one_year"));
            res.put("six_month", queryTopFunds("six_month"));
            res.put("three_month", queryTopFunds("three_month"));
            res.put("one_month", queryTopFunds("one_month"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }

        return ResponseEntity.ok(res);
    }

    private List<List<Object>> queryTopFunds(String column) {
        String sql = String.format(
                "SELECT fund_company.company_name AS cname, fund_name.fund_name AS fname, fund.fund_id AS fid, " +
                        "ROUND(fund.%s, 2) AS price FROM fund_name " +
                        "JOIN fund_company ON fund_name.company_id = fund_company.company_id " +
                        "JOIN fund ON fund_name.fund_id = fund.fund_id " +
                        "ORDER BY fund.%s DESC LIMIT 5;", column, column);

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                List.of(rs.getInt("fid"), rs.getString("cname"), rs.getString("fname"), rs.getDouble("price"))
        );
    }

    public ResponseEntity<?> loadFund(String fundId) {
        if (fundId == null || fundId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fund ID required"));
        }

        Map<String, Object> res = new HashMap<>();

        try {
            String infoSql = "SELECT fund.one_week, fund.one_month, fund.three_month, fund.six_month, " +
                    "fund.one_year, fund.lifetime, fund.value, fund.standard_deviation, " +
                    "fund_company.company_name, fund_category.category_name, fund_name.fund_name, fund.fund_id " +
                    "FROM fund " +
                    "JOIN fund_name ON (fund.fund_id = fund_name.fund_id AND fund.fund_id = ?) " +
                    "JOIN fund_company ON fund_name.company_id = fund_company.company_id " +
                    "JOIN fund_category ON fund_category.category_id = fund_name.category_id;";

            Map<String, Object> info = jdbcTemplate.queryForMap(infoSql, fundId);
            res.put("info", info);

            Map<String, Object> ids = jdbcTemplate.queryForMap(
                    "SELECT company_id, category_id FROM fund_name WHERE fund_id = ?;", fundId);

            res.put("same_company", jdbcTemplate.query(
                    "SELECT fund_name.fund_id AS fid, fund_name.fund_name AS fname, ROUND(fund.one_year, 2) AS one_year " +
                            "FROM fund_name JOIN fund ON fund_name.fund_id = fund.fund_id " +
                            "WHERE fund_name.company_id = ? AND fund_name.fund_id != ? " +
                            "ORDER BY fund.fund_rank LIMIT 5;",
                    (rs, rowNum) -> List.of(rs.getInt("fid"), rs.getString("fname"), rs.getDouble("one_year")),
                    ids.get("company_id"), fundId));

            res.put("same_category", jdbcTemplate.query(
                    "SELECT fund_name.fund_id AS fid, fund_name.fund_name AS fname, ROUND(fund.one_year, 2) AS one_year " +
                            "FROM fund_name JOIN fund ON fund_name.fund_id = fund.fund_id " +
                            "WHERE fund_name.category_id = ? AND fund_name.fund_id != ? " +
                            "ORDER BY fund.fund_category_rank LIMIT 5;",
                    (rs, rowNum) -> List.of(rs.getInt("fid"), rs.getString("fname"), rs.getDouble("one_year")),
                    ids.get("category_id"), fundId));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }

        return ResponseEntity.ok(res);
    }

    public ResponseEntity<?> loadGraphData(String fundId) {
        if (fundId == null || fundId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fund ID required"));
        }

        try {
            String sql = "SELECT date, price FROM fund_value WHERE fund_id = ? ORDER BY date DESC";
            List<List<Object>> history = jdbcTemplate.query(sql, (rs, rowNum) ->
                    List.of(rs.getDate("date").toString(), rs.getDouble("price")), fundId);
            return ResponseEntity.ok(Map.of("history", history));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not process query"));
        }
    }
}
