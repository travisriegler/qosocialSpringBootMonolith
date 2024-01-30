package com.qosocial.v1api.profile.mapper;

import com.qosocial.v1api.profile.dto.CreateProfileDto;
import com.qosocial.v1api.profile.dto.ProfileDto;
import com.qosocial.v1api.profile.model.ProfileModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

@Component
public class ProfileMapper {

    @Value("${base.url}")
    private String baseUrl;

    public ProfileModel fromCreateProfileDtoToProfileModel(CreateProfileDto createProfileDto) {

        ProfileModel profileModel = new ProfileModel();

        profileModel.setUsername(createProfileDto.getUsername());
        profileModel.setBio(createProfileDto.getBio());

        profileModel.setPostModels(new HashSet<>());

        Instant now = Instant.now();
        profileModel.setCreatedAt(now);
        profileModel.setUpdatedAt(now);

        return profileModel;

    }

    public Optional<ProfileDto> fromProfileModelToProfileDto(ProfileModel profileModel, Long myProfileId) {
        try {
            ProfileDto profileDto = new ProfileDto();

            //set the standard fields
            profileDto.setId(profileModel.getId());
            profileDto.setUsername(profileModel.getUsername());
            profileDto.setBio(profileModel.getBio());
            profileDto.setCreatedAt(profileModel.getCreatedAt());

            // Set the pictureUrl
            String pictureUrl = profileModel.getPictureUrl();
            if (pictureUrl != null && !pictureUrl.isEmpty()) {
                pictureUrl = baseUrl + pictureUrl;
            } else {
                pictureUrl = "";
            }

            profileDto.setPictureUrl(pictureUrl);

            return Optional.of(profileDto);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
