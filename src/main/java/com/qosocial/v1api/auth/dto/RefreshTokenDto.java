package com.qosocial.v1api.auth.dto;

import java.time.Instant;

public class RefreshTokenDto {
    private final String tokenId;
    private final String refreshToken;
    private final Instant expirationTime;

    public RefreshTokenDto(String tokenId, String refreshToken, Instant expirationTime) {
        this.tokenId = tokenId;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }

    public String getTokenId() { return tokenId; }
    public String getRefreshToken() { return refreshToken; }
    public Instant getExpirationTime() { return expirationTime; }
}
