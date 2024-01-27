package com.qosocial.v1api.post.service;

import com.qosocial.v1api.common.exception.InvalidJwtSubjectException;
import com.qosocial.v1api.post.dto.CreatePostDto;
import com.qosocial.v1api.post.dto.PostDto;
import com.qosocial.v1api.post.dto.UpdateDeletedDto;
import com.qosocial.v1api.post.exception.GenericCreatePostException;
import com.qosocial.v1api.post.exception.GenericGetPostsException;
import com.qosocial.v1api.post.exception.UnauthorizedToModifyPostException;
import com.qosocial.v1api.post.mapper.PostMapper;
import com.qosocial.v1api.post.model.PostModel;
import com.qosocial.v1api.post.repository.PostRepository;
import com.qosocial.v1api.profile.exception.GenericGetMyProfileException;
import com.qosocial.v1api.profile.exception.ProfileNotFoundException;
import com.qosocial.v1api.profile.model.ProfileModel;
import com.qosocial.v1api.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private ProfileService profileService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private Jwt jwtToken;



    @Test
    void createPost_ValidArguments_ReturnsVoid() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto();
        PostModel postModel = new PostModel();
        ProfileModel profileModel = new ProfileModel();

        when(postMapper.fromCreatePostDtoToPostModel(createPostDto)).thenReturn(postModel);
        when(profileService.getMyProfileModel(jwtToken)).thenReturn(profileModel);
        // postRepository does not return a value, so no need to do when() for it

        // Act
        postService.createPost(createPostDto, jwtToken);

        // Assert
        assertThat(postModel.getProfileModel()).isEqualTo(profileModel);

        // Verify
        verify(postMapper).fromCreatePostDtoToPostModel(createPostDto);
        verify(profileService).getMyProfileModel(jwtToken);
        verify(postRepository, times(1)).save(postModel);

    }

    @Test
    void createPost_InvalidJwtSubject_ThrowsInvalidJwtSubjectException() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto();
        PostModel postModel = new PostModel();
        ProfileModel profileModel = new ProfileModel();

        when(postMapper.fromCreatePostDtoToPostModel(createPostDto)).thenReturn(postModel);
        when(profileService.getMyProfileModel(jwtToken)).thenThrow(InvalidJwtSubjectException.class);
        // postRepository does not return a value, so no need to do when() for it

        // Act and Assert
        assertThatThrownBy(() -> postService.createPost(createPostDto, jwtToken))
                .isInstanceOf(InvalidJwtSubjectException.class);

        // Verify
        verify(postMapper).fromCreatePostDtoToPostModel(createPostDto);
        verify(profileService).getMyProfileModel(jwtToken);

    }

    @Test
    void createPost_ProfileNotFoundException_ThrowsInvalidJwtSubjectException() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto();
        PostModel postModel = new PostModel();
        ProfileModel profileModel = new ProfileModel();

        when(postMapper.fromCreatePostDtoToPostModel(createPostDto)).thenReturn(postModel);
        when(profileService.getMyProfileModel(jwtToken)).thenThrow(ProfileNotFoundException.class);
        // postRepository does not return a value, so no need to do when() for it

        // Act and Assert
        assertThatThrownBy(() -> postService.createPost(createPostDto, jwtToken))
                .isInstanceOf(GenericCreatePostException.class);

        // Verify
        verify(postMapper).fromCreatePostDtoToPostModel(createPostDto);
        verify(profileService).getMyProfileModel(jwtToken);

    }

    @Test
    void createPost_GenericCreatePostException_ThrowsInvalidJwtSubjectException() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto();
        PostModel postModel = new PostModel();
        ProfileModel profileModel = new ProfileModel();

        when(postMapper.fromCreatePostDtoToPostModel(createPostDto)).thenReturn(postModel);
        when(profileService.getMyProfileModel(jwtToken)).thenThrow(GenericGetMyProfileException.class);
        // postRepository does not return a value, so no need to do when() for it

        // Act and Assert
        assertThatThrownBy(() -> postService.createPost(createPostDto, jwtToken))
                .isInstanceOf(GenericCreatePostException.class);

        // Verify
        verify(postMapper).fromCreatePostDtoToPostModel(createPostDto);
        verify(profileService).getMyProfileModel(jwtToken);

    }

    @Test
    void createPost_DataIntegrityViolationException_ThrowsGenericCreatePostException() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto();
        PostModel postModel = new PostModel();
        ProfileModel profileModel = new ProfileModel();

        when(postMapper.fromCreatePostDtoToPostModel(createPostDto)).thenReturn(postModel);
        when(profileService.getMyProfileModel(jwtToken)).thenReturn(profileModel);
        when(postRepository.save(postModel)).thenThrow(new DataIntegrityViolationException("Database error"));

        // Act
        assertThatThrownBy(() -> postService.createPost(createPostDto, jwtToken))
                .isInstanceOf(GenericCreatePostException.class);

        // Assert
        assertThat(postModel.getProfileModel()).isEqualTo(profileModel);

        // Verify
        verify(postMapper).fromCreatePostDtoToPostModel(createPostDto);
        verify(profileService).getMyProfileModel(jwtToken);
        verify(postRepository, times(1)).save(postModel);

    }

    @Test
    void createPost_NullPointerException_ThrowsGenericCreatePostException() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto();
        PostModel postModel = new PostModel();
        ProfileModel profileModel = new ProfileModel();

        when(postMapper.fromCreatePostDtoToPostModel(createPostDto)).thenThrow(new NullPointerException("Null value"));

        // Act
        assertThatThrownBy(() -> postService.createPost(createPostDto, jwtToken))
                .isInstanceOf(GenericCreatePostException.class);

        // Verify
        verify(postMapper).fromCreatePostDtoToPostModel(createPostDto);

    }

    @Test
    void getAllPosts_ValidArguments_ReturnsListPostDto() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository, postMapper

        // Arrange
        Instant timestamp = Instant.now();
        int limit = 5;
        Long myProfileId = 5L;
        List<PostModel> expectedPostModels = Arrays.asList(new PostModel(), new PostModel());
        List<PostDto> expectedPostDtos = Arrays.asList(new PostDto(), new PostDto());

        when(profileService.getMyProfileModelId(jwtToken)).thenReturn(myProfileId);
        when(postRepository.findMyPostsAndOthersNotDeletedBeforeTimestamp(eq(myProfileId), eq(timestamp), any(Pageable.class))).thenReturn(expectedPostModels);
        when(postMapper.fromPostModelToPostDto(any(PostModel.class), eq(myProfileId)))
                .thenAnswer(invocation -> Optional.of(new PostDto()));

        // Act
        List<PostDto> postDtos = postService.getAllPosts(timestamp, limit, jwtToken);

        // Assert
        assertThat(postDtos).hasSameSizeAs(expectedPostDtos);

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, times(1)).findMyPostsAndOthersNotDeletedBeforeTimestamp(eq(myProfileId), eq(timestamp), any(Pageable.class));
        verify(postMapper, times(2)).fromPostModelToPostDto(any(PostModel.class), eq(myProfileId));
    }

    @Test
    void getAllPosts_InvalidJwtSubject_ThrowsInvalidJwtSubjectException() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository, postMapper

        // Arrange
        Instant timestamp = Instant.now();
        int limit = 5;

        when(profileService.getMyProfileModelId(jwtToken)).thenThrow(InvalidJwtSubjectException.class);

        // Act and Assert
        assertThatThrownBy(() -> postService.getAllPosts(timestamp, limit, jwtToken))
                .isInstanceOf(InvalidJwtSubjectException.class);

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, never()).findMyPostsAndOthersNotDeletedBeforeTimestamp(any(Long.class), any(Instant.class), any(Pageable.class));
        verify(postMapper, never()).fromPostModelToPostDto(any(PostModel.class), any(Long.class));
    }

    @Test
    void getAllPosts_ProfileNotFoundException_ThrowsGenericGetPostsException() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository, postMapper

        // Arrange
        Instant timestamp = Instant.now();
        int limit = 5;

        when(profileService.getMyProfileModelId(jwtToken)).thenThrow(ProfileNotFoundException.class);

        // Act and Assert
        assertThatThrownBy(() -> postService.getAllPosts(timestamp, limit, jwtToken))
                .isInstanceOf(GenericGetPostsException.class);

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, never()).findMyPostsAndOthersNotDeletedBeforeTimestamp(any(Long.class), any(Instant.class), any(Pageable.class));
        verify(postMapper, never()).fromPostModelToPostDto(any(PostModel.class), any(Long.class));
    }

    @Test
    void getAllPosts_GenericGetMyProfileException_ThrowsGenericGetPostsException() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository, postMapper

        // Arrange
        Instant timestamp = Instant.now();
        int limit = 5;

        when(profileService.getMyProfileModelId(jwtToken))
                .thenThrow(GenericGetMyProfileException.class);

        // Act and Assert
        assertThatThrownBy(() -> postService.getAllPosts(timestamp, limit, jwtToken))
                .isInstanceOf(GenericGetPostsException.class);

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, never()).findMyPostsAndOthersNotDeletedBeforeTimestamp(any(Long.class), any(Instant.class), any(Pageable.class));
        verify(postMapper, never()).fromPostModelToPostDto(any(PostModel.class), any(Long.class));
    }

    @Test
    void getAllPosts_DataAccessException_ThrowsGenericGetPostsException() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository, postMapper

        // Arrange
        Instant timestamp = Instant.now();
        int limit = 5;
        Long myProfileId = 5L;

        when(profileService.getMyProfileModelId(jwtToken)).thenReturn(myProfileId);
        when(postRepository.findMyPostsAndOthersNotDeletedBeforeTimestamp(eq(myProfileId), eq(timestamp), any(Pageable.class)))
                .thenThrow(DataAccessResourceFailureException.class);

        // Act and Assert
        assertThatThrownBy(() -> postService.getAllPosts(timestamp, limit, jwtToken))
                .isInstanceOf(GenericGetPostsException.class);

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, times(1)).findMyPostsAndOthersNotDeletedBeforeTimestamp(eq(myProfileId), eq(timestamp), any(Pageable.class));
        verify(postMapper, never()).fromPostModelToPostDto(any(PostModel.class), any(Long.class));
    }

    @Test
    void getAllPosts_BadPostModel_ReturnsSmallerListPostDto() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository, postMapper

        // Arrange
        Instant timestamp = Instant.now();
        int limit = 5;
        Long myProfileId = 5L;
        PostModel goodPostModel = new PostModel();
        PostModel badPostModel = new PostModel();
        List<PostModel> expectedPostModels = Arrays.asList(goodPostModel, badPostModel);
        List<PostDto> expectedPostDtos = Arrays.asList(new PostDto(), new PostDto());

        when(profileService.getMyProfileModelId(jwtToken)).thenReturn(myProfileId);
        when(postRepository.findMyPostsAndOthersNotDeletedBeforeTimestamp(eq(myProfileId), eq(timestamp), any(Pageable.class))).thenReturn(expectedPostModels);
        when(postMapper.fromPostModelToPostDto(eq(goodPostModel), eq(myProfileId)))
                .thenReturn(Optional.of(new PostDto()));
        when(postMapper.fromPostModelToPostDto(eq(badPostModel), eq(myProfileId)))
                .thenReturn(Optional.empty());

        // Act
        List<PostDto> postDtos = postService.getAllPosts(timestamp, limit, jwtToken);

        // Assert
        assertThat(postDtos).hasSize(1);

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, times(1)).findMyPostsAndOthersNotDeletedBeforeTimestamp(eq(myProfileId), eq(timestamp), any(Pageable.class));
        verify(postMapper, times(2)).fromPostModelToPostDto(any(PostModel.class), eq(myProfileId));
    }

    // Choosing to skip tests for getPostsByProfileId because it is nearly identical to getAllPosts

    @Test
    void updateDeletePostById_ValidTrueArguments_ReturnsVoid() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository

        // Arrange
        UpdateDeletedDto updateDeletedDto = new UpdateDeletedDto(true);
        Long postId = 3L;

        Long myProfileId = 5L;
        ProfileModel myProfileModel = new ProfileModel();
        myProfileModel.setId(myProfileId);

        PostModel expectedPostModel = new PostModel();
        expectedPostModel.setProfileModel(myProfileModel);

        when(profileService.getMyProfileModelId(jwtToken)).thenReturn(myProfileId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPostModel));
        //postRepository.save returns void, so no need to call when()

        // Act
        postService.updateDeletePostById(updateDeletedDto, postId, jwtToken);

        // Assert
        assertEquals(updateDeletedDto.getWantsToDelete(), expectedPostModel.isDeleted());

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(expectedPostModel);
    }

    @Test
    void updateDeletePostById_ValidFalseArguments_ReturnsVoid() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository

        // Arrange
        UpdateDeletedDto updateDeletedDto = new UpdateDeletedDto(false);
        Long postId = 3L;

        Long myProfileId = 5L;
        ProfileModel myProfileModel = new ProfileModel();
        myProfileModel.setId(myProfileId);

        PostModel expectedPostModel = new PostModel();
        expectedPostModel.setProfileModel(myProfileModel);

        when(profileService.getMyProfileModelId(jwtToken)).thenReturn(myProfileId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPostModel));
        //postRepository.save returns void, so no need to call when()

        // Act
        postService.updateDeletePostById(updateDeletedDto, postId, jwtToken);

        // Assert
        assertEquals(updateDeletedDto.getWantsToDelete(), expectedPostModel.isDeleted());

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(expectedPostModel);
    }

    @Test
    void updateDeletePostById_InvalidNotMyPost_ThrowsUnauthorizedToModifyPostException() throws Exception {
        // Mocked resources: profileService, jwtToken, postRepository

        // Arrange
        UpdateDeletedDto updateDeletedDto = new UpdateDeletedDto(true);
        Long postId = 3L;

        Long myProfileId = 5L;

        Long postProfileId = 55L;
        ProfileModel postProfileModel = new ProfileModel();
        postProfileModel.setId(postProfileId);

        PostModel expectedPostModel = new PostModel();
        expectedPostModel.setProfileModel(postProfileModel);

        when(profileService.getMyProfileModelId(jwtToken)).thenReturn(myProfileId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPostModel));
        //postRepository.save returns void, so no need to call when()

        // Act and Assert
        assertThatThrownBy(() -> postService.updateDeletePostById(updateDeletedDto, postId, jwtToken))
                .isInstanceOf(UnauthorizedToModifyPostException.class);

        // Verify
        verify(profileService, times(1)).getMyProfileModelId(jwtToken);
        verify(postRepository, times(1)).findById(postId);
    }
}
