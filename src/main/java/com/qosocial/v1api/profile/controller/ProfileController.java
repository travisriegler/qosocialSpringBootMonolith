package com.qosocial.v1api.profile.controller;

import com.qosocial.v1api.common.util.CommonUtil;
import com.qosocial.v1api.profile.dto.CreateProfileDto;
import com.qosocial.v1api.profile.dto.EditProfileDto;
import com.qosocial.v1api.profile.dto.ProfileDto;
import com.qosocial.v1api.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping()
    public ResponseEntity<Void> createProfile(@ModelAttribute @Validated CreateProfileDto createProfileDto, @RequestParam(value = "image", required = false) MultipartFile imageFile, Authentication authentication) {

        /*
         * spring.servlet.multipart.max-file-size=250KB
         * spring.servlet.multipart.max-request-size=300KB
         */

        // validate the imageFile
        CommonUtil.validateImageFile(imageFile);

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        // Call profileService
        profileService.createProfile(jwtToken, createProfileDto, imageFile);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<ProfileDto> getMyProfile(Authentication authentication) {

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        // Call profileService
        ProfileDto profileDto = profileService.getMyProfileDto(jwtToken);

        // Return the profile
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfileById(Authentication authentication, @PathVariable Long id) {

        // Safely checks authentication and jwtToken to ensure they are not null
        // Performing this check even though the jwtToken is not used just in case the jwtToken is invalid
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        // Call profileService
        ProfileDto profileDto = profileService.getProfileDtoById(id);

        // Return the profile
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<Void> editMyProfile(@ModelAttribute @Validated EditProfileDto editProfileDto, @RequestParam(value = "image", required = false) MultipartFile imageFile, Authentication authentication) {

        /*
         * spring.servlet.multipart.max-file-size=250KB
         * spring.servlet.multipart.max-request-size=300KB
         */

        // validate the imageFile
        CommonUtil.validateImageFile(imageFile);

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        // Call profileService
        profileService.editMyProfile(jwtToken, editProfileDto, imageFile);

        return ResponseEntity.ok().build();
    }

}
