package com.qosocial.v1api.profile.mapper;

import com.qosocial.v1api.profile.dto.CreateProfileDto;
import com.qosocial.v1api.profile.model.ProfileModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;

@Component
public class ProfileMapper {

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
}
