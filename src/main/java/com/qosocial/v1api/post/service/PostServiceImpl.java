package com.qosocial.v1api.post.service;

import com.qosocial.v1api.common.exception.InvalidJwtSubjectException;
import com.qosocial.v1api.post.dto.CreatePostDto;
import com.qosocial.v1api.post.dto.PostDto;
import com.qosocial.v1api.post.dto.UpdateDeletedDto;
import com.qosocial.v1api.post.exception.*;
import com.qosocial.v1api.post.mapper.PostMapper;
import com.qosocial.v1api.post.model.PostModel;
import com.qosocial.v1api.post.repository.PostRepository;
import com.qosocial.v1api.profile.model.ProfileModel;
import com.qosocial.v1api.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final ProfileService profileService;
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    public PostServiceImpl(PostMapper postMapper, PostRepository postRepository, ProfileService profileService) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.profileService = profileService;
    }

    @Override
    @Transactional
    public void createPost(CreatePostDto createPostDto, Jwt jwtToken) {

        try {
            PostModel postModel = postMapper.fromCreatePostDtoToPostModel(createPostDto);

            ProfileModel myProfileModel = profileService.getMyProfileModel(jwtToken);

            postModel.setProfileModel(myProfileModel);

            postRepository.save(postModel);

        } catch (InvalidJwtSubjectException ex) {
            // will be logged at global controller exception handler
            throw ex;
        } catch (Exception ex) {
            logger.error("PostServiceImpl createPost caught an unexpected exception", ex);
            throw new GenericCreatePostException(ex);
        }
    }

    @Override
    public List<PostDto> getAllPosts(Instant timeStamp, int limit, Jwt jwtToken) {

        try {
            // Get my profile id to compare to each post
            Long myProfileId = profileService.getMyProfileModelId(jwtToken);

            // Pageable helps implement infinite scrolling with the postRepository
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
            List<PostModel> posts = postRepository.findMyPostsAndOthersNotDeletedBeforeTimestamp(myProfileId, timeStamp, pageable);

            // Map each post to a postDto
            return posts.stream()
                    .map(post -> postMapper.fromPostModelToPostDto(post, myProfileId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

        } catch (InvalidJwtSubjectException ex) {
            // will be logged at global controller exception handler
            throw ex;
        } catch (Exception ex) {
            logger.error("PostServiceImpl getAllPosts caught an unexpected exception", ex);
            throw new GenericGetPostsException(ex);
        }
    }

    @Override
    public List<PostDto> getPostsByProfileId(Instant timeStamp, int limit, Long profileId, Jwt jwtToken) {

        try {
            // Get my profile id to compare to each post
            Long myProfileId = profileService.getMyProfileModelId(jwtToken);

            //if it is my profile, the postsRepository will included deleted posts
            boolean isMyProfile = myProfileId.equals(profileId);

            // Pageable helps implement infinite scrolling with the postRepository
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
            List<PostModel> posts = postRepository.findPostsByProfileAndOwnershipBeforeTimestamp(profileId, isMyProfile, timeStamp, pageable);

            // Map each post to a postDto
            return posts.stream()
                    .map(post -> postMapper.fromPostModelToPostDto(post, myProfileId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

        } catch (InvalidJwtSubjectException ex) {
            // will be logged at global controller exception handler
            throw ex;
        } catch (Exception ex) {
            logger.error("PostServiceImpl getPostsByProfileId caught an unexpected exception", ex);
            throw new GenericGetPostsException(ex);
        }
    }

    @Override
    @Transactional
    public void updateDeletePostById(UpdateDeletedDto updateDeletedDto, Long postId, Jwt jwtToken) {

        Long myProfileId = null;

        try {
            // Get my profile id to compare to the post
            myProfileId = profileService.getMyProfileModelId(jwtToken);

            // Get the post from postRepository
            PostModel postModel = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException());

            // If the post does not belong to my profile, then throw an exception
            if (!postModel.getProfileModel().getId().equals(myProfileId)) {
                throw new UnauthorizedToModifyPostException();
            }

            postModel.setDeleted(updateDeletedDto.getWantsToDelete());

            postRepository.save(postModel);

        } catch (InvalidJwtSubjectException ex) {
            // will be logged at global controller exception handler
            throw ex;
        } catch (PostNotFoundException ex) {
            logger.warn("PostServiceImpl updateDeletePostById could not find postId: " + postId);
            throw ex;
        } catch (UnauthorizedToModifyPostException ex) {
            logger.warn("ProfileId " + myProfileId + " is not authorized to modify postId " + postId);
            throw ex;
        } catch (Exception ex) {
            logger.error("PostServiceImpl updateDeletePostById caught an unexpected exception", ex);
            throw new GenericUpdatePostByIdException(ex);
        }
    }
}
