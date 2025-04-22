package com.ooad.fundapi.repository;

import java.util.Map;

public interface FundRepository {
    /**
     * Get fund price on date or nearest previous date
     *
     * @param fundId Fund ID
     * @param date Date to search for
     * @return Map containing the price or empty if not found
     */
    Map<String, Object> getFundPriceOnDate(String fundId, String date);

    /**
     * Get top performing funds
     *
     * @return Map containing the results
     */
    Map<String, Object> getTopFunds();
}