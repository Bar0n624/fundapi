package com.ooad.fundapi.repository;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "watchlist")
@IdClass(WatchlistId.class)
public class Watchlist {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "fund_id")
    private Integer fundId;
}

package com.ooad.fundapi.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WatchlistId implements Serializable {
    private Integer userId;
    private Integer fundId;
}

package com.ooad.fundapi.repository;

import com.ooad.fundapi.entity.Watchlist;
import com.ooad.fundapi.entity.WatchlistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, WatchlistId> {
    // Any custom query methods would go here
}