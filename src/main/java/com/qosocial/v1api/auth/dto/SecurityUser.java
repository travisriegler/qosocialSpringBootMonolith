package com.qosocial.v1api.auth.dto;

import com.qosocial.v1api.auth.model.AppUserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {

    private final AppUserModel appUserModel;

    public SecurityUser(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }

    public AppUserModel getJpaUserModel() {
        return appUserModel;
    }

    @Override
    public String getUsername() {
        return appUserModel.getEmail();
    }

    @Override
    public String getPassword() {
        return appUserModel.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return appUserModel.getRoleModels().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return appUserModel.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return appUserModel.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return appUserModel.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return appUserModel.isEnabled();
    }

}
