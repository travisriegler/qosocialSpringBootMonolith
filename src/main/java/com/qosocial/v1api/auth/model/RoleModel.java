package com.qosocial.v1api.auth.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role_model")
public class RoleModel {

    /**
     * Created in flyway script V2__create_role_model.sql:
     *      type: bigint
     *      auto increment
     *      primary key (not null and unique by default)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    /**
     * Created in flyway script V2__create_role_model.sql:
     *      type: varchar 255
     *      not null
     *      unique
     *      check constraint prevents the name from being an empty string or containing any whitespace characters
     */
    @Column(name = "name")
    private String name;

    /**
     * Created in flyway script V4__create_app_user_role_junction.sql:
     *      bidirectional relationship maintained by a join table
     *      appUserModels is the owner of the relationship
     *      appUserModels is created in flyway script V3__create_app_user_model.sql
     */
    @ManyToMany(mappedBy = "roleModels")
    private Set<AppUserModel> appUserModels = new HashSet<>();

    public RoleModel() {
    }

    public RoleModel(String name) {
        this.name = name;
    }

    public RoleModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AppUserModel> getAppUserModels() {
        return appUserModels;
    }

    public void setAppUserModels(Set<AppUserModel> appUserModels) {
        this.appUserModels = (appUserModels != null) ? appUserModels : new HashSet<>();
    }
}
