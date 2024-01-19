package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.dto.RefreshTokenDto;
import com.qosocial.v1api.auth.model.RoleModel;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Set;

public interface JwtService {
    public String generateAccessToken(Long id, String email, Set<RoleModel> roles);
    public RefreshTokenDto generateRefreshToken(Long id, String email);
    public Jwt decodeRefreshToken(String token);
    public Boolean isTokenExpired(Instant expiresAtInstant);
}
