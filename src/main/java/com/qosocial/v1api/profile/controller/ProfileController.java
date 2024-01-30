package com.qosocial.v1api.profile.controller;

import com.qosocial.v1api.common.service.CommonService;
import com.qosocial.v1api.profile.dto.CreateProfileDto;
import com.qosocial.v1api.profile.dto.EditProfileDto;
import com.qosocial.v1api.profile.dto.ProfileDto;
import com.qosocial.v1api.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 10;
    private final ProfileService profileService;
    private final CommonService commonService;
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    public ProfileController(ProfileService profileService, CommonService commonService) {
        this.profileService = profileService;
        this.commonService = commonService;
    }

    @PostMapping()
    public ResponseEntity<Void> createProfile(@ModelAttribute @Validated CreateProfileDto createProfileDto, @RequestParam(value = "image", required = false) MultipartFile imageFile, Authentication authentication) {

        /*
         * spring.servlet.multipart.max-file-size=250KB
         * spring.servlet.multipart.max-request-size=300KB
         */

        // validate the imageFile
        commonService.validateImageFile(imageFile);

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        // Call profileService
        profileService.createProfile(jwtToken, createProfileDto, imageFile);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<ProfileDto>> getAllProfiles(@RequestParam(required = false) String offset, @RequestParam(required = false) String limit, Authentication authentication) {

        Instant timeStamp = parseOffset(offset);

        int parsedLimit = parseLimit(limit);

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        List<ProfileDto> profileDtos = profileService.getAllProfiles(timeStamp, parsedLimit, jwtToken);

        return new ResponseEntity<>(profileDtos, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getMyProfile(Authentication authentication) {

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        // Call profileService
        ProfileDto profileDto = profileService.getMyProfileDto(jwtToken);

        // Return the profile
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfileById(Authentication authentication, @PathVariable Long id) {

        // Safely checks authentication and jwtToken to ensure they are not null
        // Performing this check even though the jwtToken is not used just in case the jwtToken is invalid
        Jwt jwtToken = commonService.getJwt(authentication);

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
        commonService.validateImageFile(imageFile);

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        // Call profileService
        profileService.editMyProfile(jwtToken, editProfileDto, imageFile);

        return ResponseEntity.ok().build();
    }

    private int parseLimit(String limit) {
        try {
            int parsedLimit = Integer.parseInt(limit);
            if (parsedLimit >= MIN_LIMIT && parsedLimit <= MAX_LIMIT) {
                return parsedLimit;
            } else {
                logger.warn("ProfileController received an out-of-range limit value: " + limit);
            }
        } catch (Exception ex) {
            logger.warn("ProfileController received an invalid limit value: " + limit);
        }
        return DEFAULT_LIMIT;
    }

    private Instant parseOffset(String offset) {
        try {
            return Instant.parse(offset);
        } catch (Exception ex) {
            logger.warn("ProfileController received an invalid offset value: " + offset);
            return Instant.now();
        }
    }

}
