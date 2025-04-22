package com.ooad.fundapi.repository;

import java.sql.Date;

public interface AuthRepository {
    void invalidateToken(int userId);
    void storeToken(int userId, String token, Date createdOn);
}
