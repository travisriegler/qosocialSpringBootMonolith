package com.qosocial.v1api.profile.dto;

public class ProfileDto {

    private Long id;

    private String username;
    private String pictureUrl;

    private String bio;

    public ProfileDto() {
    }

    public ProfileDto(Long id, String username, String pictureUrl, String bio) {
        this.id = id;
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.bio = bio;
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
}
