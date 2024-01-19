package com.qosocial.v1api.post.repository;

import com.qosocial.v1api.post.model.PostModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<PostModel, Long> {

    @Query("SELECT p FROM PostModel p WHERE (p.profileModel.id = :myProfileId OR (p.deleted = false AND p.profileModel.id != :myProfileId)) AND p.createdAt < :timeStamp ORDER BY p.createdAt DESC")
    List<PostModel> findMyPostsAndOthersNotDeletedBeforeTimestamp(Long myProfileId, Instant timeStamp, Pageable pageable);

    @Query("SELECT p FROM PostModel p WHERE p.profileModel.id = :profileId AND (:isMyProfile = true OR p.deleted = false) AND p.createdAt < :timeStamp ORDER BY p.createdAt DESC")
    List<PostModel> findPostsByProfileAndOwnershipBeforeTimestamp(Long profileId, boolean isMyProfile, Instant timeStamp, Pageable pageable);

}
