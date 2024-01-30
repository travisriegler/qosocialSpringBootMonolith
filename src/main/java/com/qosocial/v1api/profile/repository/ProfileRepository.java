package com.qosocial.v1api.profile.repository;

import com.qosocial.v1api.profile.model.ProfileModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileModel, Long> {

    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByAppUserModel_Id(Long userId);
    Optional<ProfileModel> findFirstByAppUserModel_Id(Long userId);

    @Query("SELECT p FROM ProfileModel p WHERE (p.id = :myProfileId OR (p.deleted = false AND p.id != :myProfileId)) AND p.createdAt < :timeStamp ORDER BY p.createdAt DESC")
    List<ProfileModel> findMyProfileAndOthersNotDeletedBeforeTimestamp(Long myProfileId, Instant timeStamp, Pageable pageable);
}
