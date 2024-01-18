package com.qosocial.v1api.profile.model;

import com.qosocial.v1api.auth.model.AppUserModel;
import com.qosocial.v1api.post.model.PostModel;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profile_model")
public class ProfileModel {

    /**
     * Created in flyway script V6__create_profile_model.sql:
     *      type: bigint
     *      auto increment
     *      primary key (not null and unique by default)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    /**
     * Created in flyway script V6__create_profile_model.sql:
     *      type: bigint
     *      not null
     *      on delete: cascade
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUserModel appUserModel;

    /** Created in flyway script V7__create_post_model.sql */
    @OneToMany(mappedBy = "profileModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostModel> postModels = new HashSet<>();

    /**
     * Created in flyway script V6__create_profile_model.sql:
     *      type: varchar 255
     *      not null
     *      unique
     *      check constraint prevents it from being an empty string or containing any whitespace characters
     */
    @Column(name = "username")
    private String username;

    /** Created in flyway script V6__create_profile_model.sql
     *      type: text
     */
    @Column(name = "bio")
    private String bio;

    /** Created in flyway script V6__create_profile_model.sql:
     *      type: varchar 1024
     */
    @Column(name = "picture_url")
    private String pictureUrl;

    /**
     * Created in flyway script V6__create_profile_model.sql:
     *      type: timestamp
     *      not null
     *      default: current timestamp
     */
    @Column(name = "created_at")
    private Instant createdAt;

    /** Created in flyway script V6__create_profile_model.sql:
     *      type: timestamp
     *      default: null
     *      on update: current timestamp
     */
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Created in flyway script V6__create_profile_model.sql:
     *      type: boolean
     *      not null
     *      default: false
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /** Created in flyway script V6__create_profile_model.sql:
     *      type: timestamp
     *      default: null
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    public ProfileModel() {
    }

    public ProfileModel(AppUserModel appUserModel, String username, String bio, String pictureUrl, Instant createdAt, Instant updatedAt, Boolean deleted, Instant deletedAt) {
        this.appUserModel = appUserModel;
        this.username = username;
        this.bio = bio;
        this.pictureUrl = pictureUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public ProfileModel(Long id, AppUserModel appUserModel, String username, String bio, String pictureUrl, Instant createdAt, Instant updatedAt, Boolean deleted, Instant deletedAt) {
        this.id = id;
        this.appUserModel = appUserModel;
        this.username = username;
        this.bio = bio;
        this.pictureUrl = pictureUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserModel getAppUserModel() {
        return appUserModel;
    }

    public void setAppUserModel(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }

    public Set<PostModel> getPostModels() {
        return postModels;
    }

    public void setPostModels(Set<PostModel> postModels) {
        this.postModels = (postModels != null) ? postModels : new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
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

}
