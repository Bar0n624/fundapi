package com.ooad.fundapi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FundRepositoryImpl implements FundRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FundRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, Object> getFundPriceOnDate(String fundId, String date) {
        Map<String, Object> result = new HashMap<>();

        String sql = "SELECT price FROM fund_value WHERE fund_id = ? AND date <= ? " +
                "ORDER BY date DESC LIMIT 1";

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, fundId, date);

            if (!rows.isEmpty() && rows.get(0).containsKey("price")) {
                result.put("price", rows.get(0).get("price"));
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Map<String, Object> getTopFunds() {
        Map<String, Object> result = new HashMap<>();
        List<List<Object>> resultList = new ArrayList<>();

        String sql = "SELECT fund_company.company_name AS cname, fund_name.fund_name AS fname, " +
                "fund.fund_id AS fid, fund.one_year as one_year FROM fund_name " +
                "JOIN fund_company ON fund_name.company_id = fund_company.company_id " +
                "JOIN fund ON fund_name.fund_id = fund.fund_id " +
                "ORDER BY fund.fund_rank";

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            for (Map<String, Object> row : rows) {
                List<Object> fundData = new ArrayList<>();
                fundData.add(row.get("fid"));
                fundData.add(row.get("cname"));
                fundData.add(row.get("fname"));
                fundData.add(row.get("one_year"));
                resultList.add(fundData);
            }

            result.put("results", resultList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}