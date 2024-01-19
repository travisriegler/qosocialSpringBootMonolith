package com.qosocial.v1api.auth.dto;

import java.util.HashSet;
import java.util.Set;

public class LoginResponseDto {
    private String accessToken;
    private Long userId;
    private String email;
    private Set<String> roles = new HashSet<>();
    private String refreshToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String accessToken, Long userId, String email, Set<String> roles, String refreshToken) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.roles = (roles != null) ? roles : new HashSet<>();;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = (roles != null) ? roles : new HashSet<>();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

