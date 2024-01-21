package com.qosocial.v1api.post.dto;

import java.time.Instant;

public class PostDto {

    private Long id;
    private String textContent;
    private boolean isMyPost = false;

    private Long profileId;

    private String profileUsername;

    private String pictureUrl;

    private Instant createdAt;

    private boolean deleted;

    private String mediaUrl;

    public PostDto() {
    }

    public PostDto(Long id, String textContent, boolean isMyPost, Instant createdAt, Long profileId, String profileUsername, String pictureUrl, boolean deleted, String mediaUrl) {
        this.id = id;
        this.textContent = textContent;
        this.isMyPost = isMyPost;
        this.createdAt = createdAt;
        this.profileId = profileId;
        this.profileUsername = profileUsername;
        this.pictureUrl = pictureUrl;
        this.deleted = deleted;
        this.mediaUrl = mediaUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public boolean isMyPost() {
        return isMyPost;
    }

    public void setIsMyPost(boolean isMyPost) {
        this.isMyPost = isMyPost;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfileUsername() {
        return profileUsername;
    }

    public void setProfileUsername(String profileUsername) {
        this.profileUsername = profileUsername;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
