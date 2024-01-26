package com.qosocial.v1api.post.controller;

import com.qosocial.v1api.common.service.CommonService;
import com.qosocial.v1api.post.dto.PostDto;
import com.qosocial.v1api.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @Mock
    private PostService postService;
    @Mock
    private CommonService commonService;
    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwtToken;

    @InjectMocks
    private PostController postController;

    private static final int DEFAULT_LIMIT = 10;

    @Test
    void getAllPosts_ValidArguments_ReturnsPostDtos() {
         // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "5";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken);
    }

    @Test
    void getAllPosts_InvalidOffset_ReturnsPostDtos() {
        // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = "bad offset";
        String limit = "5";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken))).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken));
    }

    @Test
    void getAllPosts_NullOffset_ReturnsPostDtos() {
        // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = null;
        String limit = "5";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken))).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken));
    }

    @Test
    void getAllPosts_InvalidLargeLimit_ReturnsPostDtos() {
        // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "500";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken);
    }

    @Test
    void getAllPosts_InvalidSmallLimit_ReturnsPostDtos() {
        // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "-500";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken);
    }

    @Test
    void getAllPosts_NullLimit_ReturnsPostDtos() {
        // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = null;
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken);
    }

    @Test
    void getAllPosts_MinLimit_ReturnsPostDtos() {
        // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "1";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken);
    }

    @Test
    void getAllPosts_MaxLimit_ReturnsPostDtos() {
        // Mocked resources: postService, commonService, authentication, jwtToken

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "10";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());
        when(commonService.getJwt(authentication)).thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<PostDto>> response = postController.getAllPosts(offset, limit, authentication);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPosts);

        // Verify
        verify(commonService).getJwt(authentication);
        verify(postService).getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken);
    }

}
