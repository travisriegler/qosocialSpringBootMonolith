package com.qosocial.v1api.post.model;

import com.qosocial.v1api.profile.model.ProfileModel;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "post_model")
public class PostModel {

    /**
     * Created in flyway script V7__create_post_model.sql:
     *      type: bigint
     *      auto increment
     *      primary key (not null and unique by default)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    /**
     * Created in flyway script V7__create_post_model.sql:
     *      type: timestamp
     *      not null
     *      default: current timestamp
     */
    @Column(name = "created_at")
    private Instant createdAt;

    /** Created in flyway script V7__create_post_model.sql:
     *      type: timestamp
     *      default: null
     *      on update: current timestamp
     */
    @Column(name = "updated_at")
    private Instant updatedAt;

    /** Created in flyway script V7__create_post_model.sql:
     *      type: text
     */
    @Column(name = "text_content")
    private String textContent;

    /** Created in flyway script V7__create_post_model.sql:
     *      type: varchar 1024
     */
    @Column(name = "media_url")
    private String mediaUrl;

    /**
     * Created in flyway script V7__create_post_model.sql:
     *      type: boolean
     *      not null
     *      default: false
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * Created in flyway script V7__create_post_model.sql:
     *      type: timestamp
     *      default: null
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    /**
     * Created in flyway script V7__create_post_model.sql:
     *      type: bigint
     *      not null
     *      on delete: cascade
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileModel profileModel;

    public PostModel() {
    }

    public PostModel(Instant createdAt, Instant updatedAt, String textContent, String mediaUrl, boolean deleted, Instant deletedAt, ProfileModel profileModel) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.textContent = textContent;
        this.mediaUrl = mediaUrl;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.profileModel = profileModel;
    }

    public PostModel(Long id, Instant createdAt, Instant updatedAt, String textContent, String mediaUrl, boolean deleted, Instant deletedAt, ProfileModel profileModel) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.textContent = textContent;
        this.mediaUrl = mediaUrl;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.profileModel = profileModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public void setProfileModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }
}
