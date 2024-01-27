package com.qosocial.v1api.post.repository;

import com.qosocial.v1api.auth.model.AppUserModel;
import com.qosocial.v1api.auth.model.RoleModel;
import com.qosocial.v1api.auth.repository.AppUserRepository;
import com.qosocial.v1api.auth.repository.RoleRepository;
import com.qosocial.v1api.post.model.PostModel;
import com.qosocial.v1api.profile.model.ProfileModel;
import com.qosocial.v1api.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class PostRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    private Long postId;

    @BeforeEach
    public void setup() {
        // Create the role
        RoleModel roleModel = new RoleModel("ROLE_TEST");
        RoleModel savedRoleModel = roleRepository.save(roleModel);
        Set<RoleModel> setRoleModels = new HashSet<>();
        setRoleModels.add(savedRoleModel);

        // Create AppUserModel
        AppUserModel appUser = new AppUserModel("test@gmail.com", "MyTestPassword1!", true, setRoleModels, true, true, true, true);
        AppUserModel savedAppUserModel = appUserRepository.save(appUser);

        // Create ProfileModel
        ProfileModel profileModel = new ProfileModel(savedAppUserModel, "asdasdasdasdasdasd", "bio", "", Instant.now(), Instant.now(), false, null);
        ProfileModel savedProfileModel = profileRepository.save(profileModel);

        // Create PostModel
        PostModel post = new PostModel(Instant.now(), Instant.now(), "test post", "", false, null, savedProfileModel);
        PostModel savedPost = postRepository.save(post);
        postId = savedPost.getId();
    }

    @Test
    public void whenFindById_thenReturnPostModel() {
        // Arrange - completed in the setup method above

        // Act
        Optional<PostModel> found = postRepository.findById(postId);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(postId);
        assertThat(found.get().getTextContent()).isEqualTo("test post");

    }

}
