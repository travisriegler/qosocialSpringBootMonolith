package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.dto.RefreshTokenDto;
import com.qosocial.v1api.auth.exception.ExpiredRefreshTokenException;
import com.qosocial.v1api.auth.exception.GenericJwtGenerationException;
import com.qosocial.v1api.auth.exception.InvalidRefreshTokenException;
import com.qosocial.v1api.auth.model.RoleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder accessTokenJwtEncoder;
    private final JwtEncoder refreshTokenJwtEncoder;
    private final JwtDecoder refreshTokenJwtDecoder;
    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);


    @Autowired
    public JwtServiceImpl(@Qualifier("accessTokenJwtEncoder") JwtEncoder accessTokenJwtEncoder,
                          @Qualifier("refreshTokenJwtEncoder") JwtEncoder refreshTokenJwtEncoder,
                          @Qualifier("refreshTokenJwtDecoder") JwtDecoder refreshTokenJwtDecoder) {
        this.refreshTokenJwtDecoder = refreshTokenJwtDecoder;
        this.accessTokenJwtEncoder = accessTokenJwtEncoder;
        this.refreshTokenJwtEncoder = refreshTokenJwtEncoder;
    }

    @Override
    public String generateAccessToken(Long id, String email, Set<RoleModel> roles) {
        try {
            Instant now = Instant.now();
            Instant later = now.plus(Duration.ofMinutes(5));

            String scope = roles.stream()
                    .map(RoleModel::getName)
                    .collect(Collectors.joining(" "));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("http://self.com")
                    .audience(Collections.singletonList("my app"))
                    .issuedAt(now)
                    .expiresAt(later)
                    .subject(id.toString())
                    .claim("email", email)
                    .claim("roles", scope)
                    .build();

            return accessTokenJwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        } catch (Exception ex) {
            logger.error("JwtServiceImpl generateAccessToken caught an unexpected error", ex);
            throw new GenericJwtGenerationException(ex);
        }
    }

    @Override
    public RefreshTokenDto generateRefreshToken(Long id, String email) {
        try {
            String tokenId = UUID.randomUUID().toString();
            Instant now = Instant.now();
            Instant later = now.plus(Duration.ofDays(7));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("http://self.com")
                    .audience(Collections.singletonList("my app"))
                    .issuedAt(now)
                    .expiresAt(later)
                    .subject(id.toString())
                    .claim("email", email)
                    .claim("tokenId", tokenId)
                    .build();

            return new RefreshTokenDto(tokenId, refreshTokenJwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), later);

        } catch (Exception ex) {
            logger.error("JwtServiceImpl generateRefreshToken caught an unexpected error", ex);
            throw new GenericJwtGenerationException(ex);
        }
    }

    @Override
    public Jwt decodeRefreshToken(String refreshToken) {
        try {
            // Parses the JWT, validates signature, throws various exceptions if the jwt is invalid
            // DOES NOT THROW AN EXCEPTION IF THE JWT IS EXPIRED, we must manually check this
            return refreshTokenJwtDecoder.decode(refreshToken);
        } catch (Exception ex) {
            if (ex.getMessage().contains("expired")) {
                throw new ExpiredRefreshTokenException(ex);
            }
            logger.error("JwtServiceImpl decodeRefreshToken encountered an unexpected exception", ex);
            throw new InvalidRefreshTokenException(ex);
        }
    }

    @Override
    public Boolean isTokenExpired(Instant expiresAt) {
        Instant now = Instant.now();
        return expiresAt.isBefore(now);
    }
}
