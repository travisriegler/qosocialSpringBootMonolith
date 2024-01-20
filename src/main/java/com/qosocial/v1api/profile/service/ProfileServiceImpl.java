package com.qosocial.v1api.profile.service;

import com.qosocial.v1api.auth.model.AppUserModel;
import com.qosocial.v1api.auth.service.AuthService;
import com.qosocial.v1api.common.exception.InvalidImageException;
import com.qosocial.v1api.common.exception.InvalidJwtSubjectException;
import com.qosocial.v1api.common.service.ImageService;
import com.qosocial.v1api.common.util.CommonUtil;
import com.qosocial.v1api.profile.dto.CreateProfileDto;
import com.qosocial.v1api.profile.dto.EditProfileDto;
import com.qosocial.v1api.profile.dto.ProfileDto;
import com.qosocial.v1api.profile.exception.*;
import com.qosocial.v1api.profile.mapper.ProfileMapper;
import com.qosocial.v1api.profile.model.ProfileModel;
import com.qosocial.v1api.profile.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    private final AuthService authService;
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Value("${base.url}")
    private String baseUrl;

    @Autowired
    public ProfileServiceImpl(ProfileMapper profileMapper, ProfileRepository profileRepository, AuthService authService, ImageService imageService) {
        this.profileMapper = profileMapper;
        this.profileRepository = profileRepository;
        this.authService = authService;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public void createProfile(Jwt jwtToken, CreateProfileDto createProfileDto, MultipartFile imageFile) {

        Long userId = null;

        try {

            // Get the Long userId from jwtToken
            userId = CommonUtil.getLongUserId(jwtToken);

            // Check if they have a profile already (currently only allowed 1 profile)
            if (profileRepository.existsByAppUserModel_Id(userId)) { throw new ProfileAlreadyExistsException(); }

            // Check for duplicate username
            if (profileRepository.existsByUsernameIgnoreCase(createProfileDto.getUsername())) { throw new UsernameAlreadyExistsException(); }

            // Create the newProfile
            ProfileModel newProfile = profileMapper.fromCreateProfileDtoToProfileModel(createProfileDto);

            // Find the appUserModel using the userId
            AppUserModel appUserModel = authService.findById(userId);

            // Add the appUserModel to the newProfile
            newProfile.setAppUserModel(appUserModel);

            // If we received an image, save it
            String imagePath = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imagePath = imageService.saveImage(imageFile);
                newProfile.setPictureUrl(imagePath);
            }

            // Save the newProfile
            profileRepository.save(newProfile);

        } catch (InvalidJwtSubjectException | InvalidImageException ex) {
            // InvalidJwtSubjectException will be logged at global controller exception handler
            // InvalidImageException was already logged in imageService.saveImage
            throw ex;
        } catch (ProfileAlreadyExistsException ex) {
            logger.warn("User " + userId + " tried to create more than 1 profile");
            throw ex;
        } catch (UsernameAlreadyExistsException ex) {
            logger.warn("Requested username " + createProfileDto.getUsername() + " already exists");
            throw ex;
        } catch (Exception ex) {
            logger.error("ProfileServiceImpl createProfile caught an unexpected error", ex);
            throw new GenericCreateProfileException(ex);
        }
    }

    @Override
    public ProfileDto getMyProfileDto(Jwt jwtToken) {

        Long myUserId = null;

        try {
            // Get the Long userId from jwtToken
            myUserId = CommonUtil.getLongUserId(jwtToken);

            // Call profileRepository to find a profile associated with the userId
            ProfileModel myProfileModel = profileRepository.findFirstByAppUserModel_Id(myUserId).orElseThrow(() -> new ProfileNotFoundException());

            // Set the pictureUrl
            String pictureUrl = myProfileModel.getPictureUrl();
            if (pictureUrl != null && !pictureUrl.isEmpty()) {
                pictureUrl = baseUrl + pictureUrl;
            } else {
                pictureUrl = "";
            }

            return new ProfileDto(myProfileModel.getId(), myProfileModel.getUsername(), pictureUrl, myProfileModel.getBio());

        } catch (InvalidJwtSubjectException ex) {
            // will be logged at global controller exception handler
            throw ex;
        } catch (ProfileNotFoundException ex) {
            logger.warn("Profile not found for userId: " + myUserId);
            throw ex;
        } catch (Exception ex) {
            logger.error("ProfileServiceImpl getMyProfileDto caught an unexpected error", ex);
            throw new GenericGetMyProfileException(ex);
        }
    }

    @Override
    public ProfileModel getMyProfileModel(Jwt jwtToken) {

        Long myUserId = null;

        try {
            // Get the Long userId from jwtToken
            myUserId = CommonUtil.getLongUserId(jwtToken);

            // Call profileRepository to find a profile associated with the userId. Could throw ProfileNotFoundException
            return profileRepository.findFirstByAppUserModel_Id(myUserId).orElseThrow(() -> new ProfileNotFoundException());

        }  catch (InvalidJwtSubjectException ex) {
            // will be logged at global controller exception handler
            throw ex;
        } catch (ProfileNotFoundException ex) {
            logger.warn("Profile not found for userId: " + myUserId);
            throw ex;
        } catch (Exception ex) {
            logger.error("ProfileServiceImpl getMyProfileModel caught an unexpected error", ex);
            throw new GenericGetMyProfileException(ex);
        }
    }


    @Override
    public Long getMyProfileModelId(Jwt jwtToken) {

        Long myUserId = null;

        try {
            // Get the Long userId from jwtToken
            myUserId = CommonUtil.getLongUserId(jwtToken);

            // Call profileRepository to find a profile associated with the userId. Could throw ProfileNotFoundException
            ProfileModel myProfileModel = profileRepository.findFirstByAppUserModel_Id(myUserId).orElseThrow(() -> new ProfileNotFoundException());

            return myProfileModel.getId();

        }  catch (InvalidJwtSubjectException ex) {
            // will be logged at global controller exception handler
            throw ex;
        } catch (ProfileNotFoundException ex) {
            logger.warn("Profile not found for userId: " + myUserId);
            throw ex;
        } catch (Exception ex) {
            logger.error("ProfileServiceImpl getMyProfileModelId caught an unexpected error", ex);
            throw new GenericGetMyProfileException(ex);
        }
    }

    @Override
    public ProfileDto getProfileDtoById(Long id) {
        try {
            ProfileModel profileModel = profileRepository.findById(id).orElseThrow(() -> new ProfileNotFoundException());

            String pictureUrl = profileModel.getPictureUrl();
            if (pictureUrl != null && !pictureUrl.isEmpty()) {
                pictureUrl = baseUrl + pictureUrl;
            } else {
                pictureUrl = "";
            }

            return new ProfileDto(profileModel.getId(), profileModel.getUsername(), pictureUrl, profileModel.getBio());

        } catch (ProfileNotFoundException ex) {
            logger.warn("Profile not found for profileId: " + id);
            throw ex;
        } catch (Exception ex) {
            logger.error("ProfileServiceImpl getProfileDtoById caught an unexpected error", ex);
            throw new GenericGetProfileException(ex);
        }
    }

    @Override
    @Transactional
    public void editMyProfile(Jwt jwtToken, EditProfileDto editProfileDto, MultipartFile imageFile) {

        try {
            // Get the Long userId from jwtToken
            Long myUserId = CommonUtil.getLongUserId(jwtToken);

            // Find myProfileModel
            ProfileModel myProfileModel = profileRepository.findFirstByAppUserModel_Id(myUserId).orElseThrow(() -> new ProfileNotFoundException());

            String oldPictureToDelete = null;

            // if we did not receive an image and isDeletingPicture is true, then mark the old picture for later deletion and setPictureUrl to null
            if ((imageFile == null || imageFile.isEmpty()) && editProfileDto.isDeletingPicture()) {
                oldPictureToDelete = myProfileModel.getPictureUrl();
                myProfileModel.setPictureUrl(null);
            }

            // if we received an image, then mark the old picture for later deletion, save the new image, and add the new image to the profile
            if (imageFile != null && !imageFile.isEmpty()) {
                oldPictureToDelete = myProfileModel.getPictureUrl();
                String imagePath = imageService.saveImage(imageFile);
                myProfileModel.setPictureUrl(imagePath);
            }

            // update the remaining parts of the profile
            Instant now = Instant.now();
            myProfileModel.setUpdatedAt(now);
            myProfileModel.setBio(editProfileDto.getBio());

            // Save myProfileModel
            profileRepository.save(myProfileModel);

            // if earlier we determined we needed to delete an old picture, now we do it
            if (oldPictureToDelete != null) {
                imageService.deleteImage(oldPictureToDelete);
            }

        } catch (InvalidJwtSubjectException | InvalidImageException ex) {
            // InvalidJwtSubjectException will be logged at global controller exception handler
            // InvalidImageException was already logged by imageService.saveImage
            throw ex;
        } catch (Exception ex) {
            logger.error("ProfileServiceImpl editMyProfile caught an unexpected error", ex);
            throw new GenericEditProfileException(ex);
        }
    }
}
