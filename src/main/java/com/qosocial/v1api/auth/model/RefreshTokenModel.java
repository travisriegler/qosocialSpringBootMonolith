package com.qosocial.v1api.auth.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "refresh_token_model")
public class RefreshTokenModel {

    /**
     * Created in flyway script V5__create_refresh_token_model.sql:
     *      type: varchar 255
     *      primary key (not null and unique by default)
     *      tokenId is a UUID stored as a string for database compatibility
     */
    @Id
    @Column(name = "token_id")
    private String tokenId;

    /**
     * Created in flyway script V5__create_refresh_token_model.sql:
     *      type: timestamp
     *      not null
     *      default: current timestamp
     */
    @Column(name = "expiration_time")
    private Instant expirationTime;

    /**
     * Created in flyway script V5__create_refresh_token_model.sql:
     *      type: bigint
     *      not null
     *      on delete: cascade
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUserModel appUserModel;

    public RefreshTokenModel() {
    }

    public RefreshTokenModel(String tokenId, Instant expirationTime, AppUserModel appUserModel) {
        this.tokenId = tokenId;
        this.expirationTime = expirationTime;
        this.appUserModel = appUserModel;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public AppUserModel getAppUserModel() {
        return appUserModel;
    }

    public void setAppUserModel(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshTokenModel that = (RefreshTokenModel) o;
        return Objects.equals(tokenId, that.tokenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenId);
    }
}
