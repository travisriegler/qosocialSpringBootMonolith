package com.qosocial.v1api.auth.model;

import com.qosocial.v1api.profile.model.ProfileModel;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user_model")
public class AppUserModel {

    /**
     * Created in flyway script V3__create_app_user_model.sql:
     *      type: bigint
     *      auto increment
     *      primary key (not null and unique by default)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * Created in flyway script V3__create_app_user_model.sql:
     *      type: varchar 255
     *      not null
     *      unique
     *      check constraint prevents it from being an empty string or containing any whitespace characters
     */
    @Column(name = "email")
    private String email;

    /**
     * Created in flyway script V3__create_app_user_model.sql:
     *      type: varchar 255
     *      not null
     *      check constraint prevents it from being an empty string or containing any whitespace characters
     */
    @Column(name = "password")
    private String password;

    /**
     * Created in flyway script V3__create_app_user_model.sql:
     *      type: boolean
     *      not null
     *      default: false
     */
    @Column(name = "accepted_terms")
    private boolean acceptedTerms = false;

    /**
     * Created in flyway script V4__create_app_user_role_junction.sql:
     *      bidirectional relationship maintained by a join table
     *      appUserModels is the owner of the relationship
     *      roleModels is created in flyway script V2__create_role_model.sql
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "app_user_role_junction", joinColumns = {@JoinColumn(name="user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<RoleModel> roleModels = new HashSet<>();

    /**
     * Created in flyway script V3__create_app_user_model.sql:
     *      type: boolean
     *      not null
     *      default: false
     */
    @Column(name = "account_non_expired")
    private boolean accountNonExpired = false;

    /**
     * Created in flyway script V3__create_app_user_model.sql:
     *      type: boolean
     *      not null
     *      default: false
     */
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = false;

    /**
     * Created in flyway script V3__create_app_user_model.sql:
     *      type: boolean
     *      not null
     *      default: false
     */
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = false;

    /**
     * Created and in flyway script V3__create_app_user_model.sql:
     *      type: boolean
     *      not null
     *      default: false
     */
    @Column(name = "enabled")
    private boolean enabled = false;

    /** Created in flyway script V5__create_refresh_token_model.sql */
    @OneToMany(mappedBy = "appUserModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshTokenModel> refreshTokenModels = new HashSet<>();

    /** Created in flyway script V6__create_profile_model.sql */
    @OneToMany(mappedBy = "appUserModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfileModel> profileModels = new HashSet<>();

    public AppUserModel() {
    }

    public AppUserModel(String email, String password, boolean acceptedTerms, Set<RoleModel> roleModels, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.email = email;
        this.password = password;
        this.acceptedTerms = acceptedTerms;
        this.roleModels = (roleModels != null) ? roleModels : new HashSet<>();
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public AppUserModel(Long id, String email, String password, boolean acceptedTerms, Set<RoleModel> roleModels, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.acceptedTerms = acceptedTerms;
        this.roleModels = (roleModels != null) ? roleModels : new HashSet<>();
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }

    public Set<RoleModel> getRoleModels() {
        return roleModels;
    }

    public void setRoleModels(Set<RoleModel> roleModels) {
        this.roleModels = (roleModels != null) ? roleModels : new HashSet<>();
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<RefreshTokenModel> getRefreshTokenModels() {
        return refreshTokenModels;
    }

    public void setRefreshTokenModels(Set<RefreshTokenModel> refreshTokenModels) {
        this.refreshTokenModels = (refreshTokenModels != null) ? refreshTokenModels : new HashSet<>();
    }

    public Set<ProfileModel> getProfileModels() {
        return profileModels;
    }

    public void setProfileModels(Set<ProfileModel> profileModels) {
        this.profileModels = (profileModels != null) ? profileModels : new HashSet<>();
    }
}
