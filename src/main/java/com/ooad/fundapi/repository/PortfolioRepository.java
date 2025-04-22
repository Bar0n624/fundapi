package com.ooad.fundapi.repository;

import java.util.List;

public interface PortfolioRepository {
    List<List<Object>> getPortfolioByUserId(Integer userId);
    boolean addPortfolioItem(Integer userId, Integer fundId, String boughtOn, Double boughtFor,
                             Double investedAmount, String soldOn, Double soldFor, Double returnAmount);
    boolean updatePortfolioItem(Integer userId, Integer fundId, String boughtOn,
                                String soldOn, Double soldFor, Double returnAmount);
    boolean deletePortfolioItem(Integer userId, Integer fundId, String boughtOn);
}