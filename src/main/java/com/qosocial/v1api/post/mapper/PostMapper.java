package com.qosocial.v1api.post.mapper;

import com.qosocial.v1api.post.dto.CreatePostDto;
import com.qosocial.v1api.post.dto.PostDto;
import com.qosocial.v1api.post.model.PostModel;
import com.qosocial.v1api.profile.dto.ProfileDto;
import com.qosocial.v1api.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class PostMapper {

    private final ProfileService profileService;

    @Autowired
    public PostMapper(ProfileService profileService) {
        this.profileService = profileService;
    }

    public PostModel fromCreatePostDtoToPostModel(CreatePostDto createPostDto) {

        PostModel postModel = new PostModel();

        postModel.setTextContent(createPostDto.getTextContent());

        Instant currentDateTime = Instant.now();
        postModel.setCreatedAt(currentDateTime);
        postModel.setUpdatedAt(currentDateTime);

        return postModel;
    }

    public Optional<PostDto> fromPostModelToPostDto(PostModel post, Long myProfileId) {

        try {
            PostDto postDto = new PostDto();

            //set the standard fields
            postDto.setId(post.getId());
            postDto.setTextContent(post.getTextContent());
            postDto.setCreatedAt(post.getCreatedAt());
            postDto.setDeleted(post.isDeleted());

            //try to get information on the associated profile
            Long postProfileId = post.getProfileModel().getId();
            postDto.setIsMyPost(postProfileId.equals(myProfileId));
            ProfileDto postProfileResponseDto = profileService.getProfileDtoById(postProfileId);

            //set the profile related fields
            postDto.setPictureUrl(postProfileResponseDto.getPictureUrl());
            postDto.setProfileId(postProfileResponseDto.getId());
            postDto.setProfileUsername(postProfileResponseDto.getUsername());

            return Optional.of(postDto);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
