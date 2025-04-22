package com.ooad.fundapi.repository;

import java.util.List;

public interface WatchlistRepository {
    List<List<Object>> getWatchlistByUserId(Integer userId);
    boolean addWatchlistItem(Integer userId, Integer fundId);
    boolean deleteWatchlistItem(Integer userId, Integer fundId);
    int[] addMultipleWatchlistItems(List<Object[]> items);
}