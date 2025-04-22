package com.ooad.fundapi.repository.impl;

import com.ooad.fundapi.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WatchlistRepositoryImpl implements WatchlistRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<List<Object>> getWatchlistByUserId(Integer userId) {
        String query = "SELECT fund_name.fund_id AS fid, fund_name.fund_name AS fname, " +
                "ROUND(fund.one_year, 2) AS one_year, ROUND(fund.one_day, 2) AS one_day " +
                "FROM fund_name " +
                "JOIN fund ON fund_name.fund_id = fund.fund_id " +
                "JOIN watchlist ON watchlist.fund_id = fund_name.fund_id " +
                "WHERE watchlist.user_id = ? " +
                "ORDER BY fund.fund_rank";

        return jdbcTemplate.query(query,
                (rs, rowNum) -> {
                    List<Object> row = new ArrayList<>();
                    row.add(rs.getInt("fid"));
                    row.add(rs.getString("fname"));
                    row.add(rs.getDouble("one_year"));
                    row.add(rs.getDouble("one_day"));
                    return row;
                },
                userId
        );
    }

    @Override
    public boolean addWatchlistItem(Integer userId, Integer fundId) {
        int result = jdbcTemplate.update(
                "INSERT INTO watchlist (user_id, fund_id) VALUES (?, ?)",
                userId, fundId
        );
        return result > 0;
    }

    @Override
    public boolean deleteWatchlistItem(Integer userId, Integer fundId) {
        int result = jdbcTemplate.update(
                "DELETE FROM watchlist WHERE user_id = ? AND fund_id = ?",
                userId, fundId
        );
        return result > 0;
    }

    @Override
    public int[] addMultipleWatchlistItems(List<Object[]> items) {
        return jdbcTemplate.batchUpdate(
                "INSERT INTO watchlist (user_id, fund_id) VALUES (?, ?)",
                items
        );
    }
}