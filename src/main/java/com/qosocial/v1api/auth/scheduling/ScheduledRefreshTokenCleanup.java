package com.qosocial.v1api.auth.scheduling;

import com.qosocial.v1api.auth.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ScheduledRefreshTokenCleanup {

    private final RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public ScheduledRefreshTokenCleanup(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void cleanExpiredTokens() {
        Instant now = Instant.now();
        List<String> expiredTokenIds = refreshTokenRepository.findExpiredTokenIds(now);

        for (String tokenId : expiredTokenIds) {
            refreshTokenRepository.deleteById(tokenId);
        }

    }
}