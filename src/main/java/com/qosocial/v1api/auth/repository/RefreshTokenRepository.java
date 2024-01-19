package com.qosocial.v1api.auth.repository;

import com.qosocial.v1api.auth.model.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, String> {

    @Query("SELECT t.tokenId FROM RefreshTokenModel t WHERE t.expirationTime < :now")
    List<String> findExpiredTokenIds(@Param("now") Instant now);

}
