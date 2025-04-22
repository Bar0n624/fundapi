package com.ooad.fundapi.repository.impl;

import com.ooad.fundapi.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PortfolioRepositoryImpl implements PortfolioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<List<Object>> getPortfolioByUserId(Integer userId) {
        String query =
                "SELECT portfolio.fund_id as fid, fund_name.fund_name as fname, " +
                        "bought_on, bought_for, invested_amount, sold_on, sold_for, return_amount, fund.value " +
                        "FROM portfolio, fund, fund_name where portfolio.fund_id = fund.fund_id AND " +
                        "portfolio.user_id = ? AND portfolio.fund_id = fund_name.fund_id " +
                        "ORDER BY invested_amount DESC";

        return jdbcTemplate.query(query,
                (rs, rowNum) -> {
                    List<Object> row = new ArrayList<>();
                    row.add(rs.getInt("fid"));
                    row.add(rs.getString("fname"));
                    row.add(rs.getDate("bought_on").toString());
                    row.add(rs.getDouble("bought_for"));
                    row.add(rs.getDouble("invested_amount"));

                    // Handle potentially null values
                    java.sql.Date soldOnDate = rs.getDate("sold_on");
                    row.add(soldOnDate != null ? soldOnDate.toString() : null);

                    Double soldFor = rs.getObject("sold_for") != null ? rs.getDouble("sold_for") : null;
                    row.add(soldFor);

                    Double returnAmount = rs.getObject("return_amount") != null ? rs.getDouble("return_amount") : null;
                    row.add(returnAmount);

                    row.add(rs.getDouble("value"));
                    return row;
                },
                userId
        );
    }

    @Override
    public boolean addPortfolioItem(Integer userId, Integer fundId, String boughtOn, Double boughtFor,
                                    Double investedAmount, String soldOn, Double soldFor, Double returnAmount) {
        int result = jdbcTemplate.update(
                "INSERT INTO portfolio (user_id, fund_id, bought_on, bought_for, invested_amount, sold_on, sold_for, return_amount) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                userId, fundId, boughtOn, boughtFor, investedAmount, soldOn, soldFor, returnAmount
        );
        return result > 0;
    }

    @Override
    public boolean updatePortfolioItem(Integer userId, Integer fundId, String boughtOn,
                                       String soldOn, Double soldFor, Double returnAmount) {
        int result = jdbcTemplate.update(
                "UPDATE portfolio SET sold_on=?, sold_for=?, return_amount=? " +
                        "WHERE user_id=? AND fund_id=? AND bought_on=?",
                soldOn, soldFor, returnAmount, userId, fundId, boughtOn
        );
        return result > 0;
    }

    @Override
    public boolean deletePortfolioItem(Integer userId, Integer fundId, String boughtOn) {
        int result = jdbcTemplate.update(
                "DELETE FROM portfolio WHERE user_id = ? AND fund_id = ? AND bought_on = ?",
                userId, fundId, boughtOn
        );
        return result > 0;
    }
}