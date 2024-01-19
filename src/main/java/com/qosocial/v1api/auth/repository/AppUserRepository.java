package com.qosocial.v1api.auth.repository;

import com.qosocial.v1api.auth.model.AppUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUserModel, Long> {
    Optional<AppUserModel> findByEmail(String email);
}
