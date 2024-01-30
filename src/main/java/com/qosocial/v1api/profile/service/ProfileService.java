package com.qosocial.v1api.profile.service;

import com.qosocial.v1api.profile.dto.CreateProfileDto;
import com.qosocial.v1api.profile.dto.EditProfileDto;
import com.qosocial.v1api.profile.dto.ProfileDto;
import com.qosocial.v1api.profile.model.ProfileModel;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

public interface ProfileService {
    public void createProfile(Jwt jwtToken, CreateProfileDto createProfileDto, MultipartFile imageFile);

    public List<ProfileDto> getAllProfiles(Instant timeStamp, int limit, Jwt jwtToken);

    public ProfileDto getMyProfileDto(Jwt jwtToken);

    public ProfileModel getMyProfileModel(Jwt jwtToken);

    public Long getMyProfileModelId(Jwt jwtToken);

    public ProfileDto getProfileDtoById(Long id);

    public void editMyProfile(Jwt jwtToken, EditProfileDto editProfileDto, MultipartFile imageFile);
}