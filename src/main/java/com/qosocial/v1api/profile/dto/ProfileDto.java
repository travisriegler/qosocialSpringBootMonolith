package com.qosocial.v1api.profile.dto;

import java.time.Instant;

public class ProfileDto {

    private Long id;

    private String username;
    private String pictureUrl;

    private String bio;

    private Instant createdAt;

    public ProfileDto() {
    }

    public ProfileDto(Long id, String username, String pictureUrl, String bio, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
