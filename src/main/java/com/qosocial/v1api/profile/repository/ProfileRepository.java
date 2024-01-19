package com.qosocial.v1api.profile.repository;

import com.qosocial.v1api.profile.model.ProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileModel, Long> {

    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByAppUserModel_Id(Long userId);
    Optional<ProfileModel> findFirstByAppUserModel_Id(Long userId);
}
