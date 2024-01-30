package com.qosocial.v1api;

import com.qosocial.v1api.auth.model.AppUserModel;
import com.qosocial.v1api.auth.model.RoleModel;
import com.qosocial.v1api.auth.repository.AppUserRepository;
import com.qosocial.v1api.auth.repository.RoleRepository;
import com.qosocial.v1api.post.model.PostModel;
import com.qosocial.v1api.post.repository.PostRepository;
import com.qosocial.v1api.profile.model.ProfileModel;
import com.qosocial.v1api.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Component
@Profile("dev")
public class DevCommandLineRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final AppUserRepository appUserRepository;

    private final ProfileRepository profileRepository;

    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DevCommandLineRunner(RoleRepository roleRepository, AppUserRepository appUserRepository, ProfileRepository profileRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.appUserRepository = appUserRepository;
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Create the role
        RoleModel roleModel = new RoleModel("ROLE_TEST");
        RoleModel savedRoleModel = roleRepository.save(roleModel);
        Set<RoleModel> setRoleModels = new HashSet<>();
        setRoleModels.add(savedRoleModel);

        // Create the Posts
        for (int i = 1; i <= 30; i++) {
            AppUserModel savedAppUserModel = createUserModel("test" + i + "@gmail.com", setRoleModels);
            ProfileModel savedProfileModel = createProfileModel(savedAppUserModel, savedAppUserModel.getEmail());
            createPostModel("Post " + i, savedProfileModel);
        }

    }

    private void createPostModel(String textContent, ProfileModel profileModel) {
        PostModel post = new PostModel(Instant.now(), Instant.now(), textContent, "", false, null, profileModel);
        PostModel savedPost = postRepository.save(post);
    }

    private ProfileModel createProfileModel(AppUserModel appUserModel, String username) {
        ProfileModel profileModel = new ProfileModel(appUserModel, username, "bio", "", Instant.now(), Instant.now(), false, null);
        return profileRepository.save(profileModel);
    }

    private AppUserModel createUserModel(String email, Set<RoleModel> setRoleModels) {
        // Create AppUserModel
        AppUserModel appUser = new AppUserModel();
        appUser.setEmail(email);
        appUser.setPassword(passwordEncoder.encode("MyTestPassword1!"));
        appUser.setAcceptedTerms(true);
        appUser.setRoleModels(setRoleModels);
        appUser.setAccountNonExpired(true);
        appUser.setAccountNonLocked(true);
        appUser.setCredentialsNonExpired(true);
        appUser.setEnabled(true);

        return appUserRepository.save(appUser);
    }
}
