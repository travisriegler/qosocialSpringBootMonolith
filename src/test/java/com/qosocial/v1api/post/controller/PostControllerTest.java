package com.qosocial.v1api.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qosocial.v1api.common.service.CommonService;
import com.qosocial.v1api.post.dto.CreatePostDto;
import com.qosocial.v1api.post.dto.PostDto;
import com.qosocial.v1api.post.dto.UpdateDeletedDto;
import com.qosocial.v1api.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {
    @MockBean
    private PostService postService;
    @MockBean
    private CommonService commonService;
    @MockBean
    private Jwt jwtToken;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_TEXT_CONTENT = 200;
    private static final int MIN_TEXT_CONTENT = 1;

    @Test
    void createPost_ValidArguments_ReturnsCreatedStatus() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // Arrange
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTextContent("Valid new post request");
        String createPostDtoJson = objectMapper.writeValueAsString(createPostDto);

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);

        doNothing()
                .when(postService).createPost(any(CreatePostDto.class), eq(jwtToken));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).createPost(any(CreatePostDto.class), eq(jwtToken));
    }

    @Test
    void createPost_ValidMaxTextLimit_ReturnsCreatedStatus() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // Arrange
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTextContent("a".repeat(MAX_TEXT_CONTENT));
        String createPostDtoJson = objectMapper.writeValueAsString(createPostDto);

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);

        doNothing()
                .when(postService).createPost(any(CreatePostDto.class), eq(jwtToken));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).createPost(any(CreatePostDto.class), eq(jwtToken));
    }

    @Test
    void createPost_ValidMinTextLimit_ReturnsCreatedStatus() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // Arrange
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTextContent("a".repeat(MIN_TEXT_CONTENT));
        String createPostDtoJson = objectMapper.writeValueAsString(createPostDto);

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        doNothing()
                .when(postService).createPost(any(CreatePostDto.class), eq(jwtToken));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).createPost(any(CreatePostDto.class), eq(jwtToken));
    }

    @Test
    void createPost_InvalidLargeTextContent_ReturnsBadRequest() throws Exception {
        // Mocked resources: commonService, postService

        // Arrange
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTextContent("a".repeat(MAX_TEXT_CONTENT + 1));
        String createPostDtoJson = objectMapper.writeValueAsString(createPostDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        verify(commonService, never()).getJwt(any(Authentication.class));
        verify(postService, never()).createPost(any(CreatePostDto.class), any(Jwt.class));
    }

    @Test
    void createPost_InvalidSmallTextContent_ReturnsBadRequest() throws Exception {
        // Mocked resources: commonService, postService

        // Arrange
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTextContent("");
        String createPostDtoJson = objectMapper.writeValueAsString(createPostDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        verify(commonService, never()).getJwt(any(Authentication.class));
        verify(postService, never()).createPost(any(CreatePostDto.class), any(Jwt.class));
    }

    @Test
    void createPost_InvalidNullTextContent_ReturnsBadRequest() throws Exception {
        // Mocked resources: postService, commonService, jwtToken

        // Arrange
        CreatePostDto createPostDto = new CreatePostDto();
        String createPostDtoJson = objectMapper.writeValueAsString(createPostDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        verify(commonService, never()).getJwt(any(Authentication.class));
        verify(postService, never()).createPost(any(CreatePostDto.class), any(Jwt.class));
    }

    @Test
    void getAllPosts_ValidArguments_ReturnsPostDtos() throws Exception {
         // Mocked resources: commonService, jwtToken, postService

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "5";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken);
    }

    @Test
    void getAllPosts_InvalidOffset_ReturnsPostDtos() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // with an invalid offset, the controller should default to Instant.now() and proceed

        // Arrange
        String offset = "bad offset";
        String limit = "5";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken)))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken));
    }

    @Test
    void getAllPosts_NullOffset_ReturnsPostDtos() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // with a null offset, the controller should default to Instant.now() and proceed

        // Arrange
        String offset = null;
        String limit = "5";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken)))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(any(Instant.class), eq(Integer.parseInt(limit)), eq(jwtToken));
    }

    @Test
    void getAllPosts_InvalidLargeLimit_ReturnsPostDtos() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // With an invalid large limit, the controller should default to 10 and proceed

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "11";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken);
    }

    @Test
    void getAllPosts_InvalidSmallLimit_ReturnsPostDtos() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // With an invalid small limit, the controller should default to 10 and proceed

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "0";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken);
    }

    @Test
    void getAllPosts_NullLimit_ReturnsPostDtos() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // With an invalid null limit, the controller should default to 10 and proceed

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = null;
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(Instant.parse(offset), DEFAULT_LIMIT, jwtToken);
    }

    @Test
    void getAllPosts_MinLimit_ReturnsPostDtos() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "1";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken);
    }

    @Test
    void getAllPosts_MaxLimit_ReturnsPostDtos() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // Arrange
        String offset = "2024-01-25T00:00:00Z";
        String limit = "10";
        List<PostDto> expectedPosts = Arrays.asList(new PostDto(), new PostDto());

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        when(postService.getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken))
                .thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("offset", offset)
                        .param("limit", limit)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPosts)));

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).getAllPosts(Instant.parse(offset), Integer.parseInt(limit), jwtToken);
    }

    // Choosing to skip tests for getPostsByProfileId because it is nearly identical to getAllPosts

    @Test
    void updateDeletePostById_ValidTrueArguments_ReturnsSuccessStatus() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // Arrange
        Long testId = 1L;
        UpdateDeletedDto updateDeletedDto = new UpdateDeletedDto(true);
        String updateDeletedDtoJson = objectMapper.writeValueAsString(updateDeletedDto);

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);

        doNothing()
                .when(postService).updateDeletePostById(any(UpdateDeletedDto.class), eq(testId), eq(jwtToken));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/delete/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDeletedDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).updateDeletePostById(any(UpdateDeletedDto.class), eq(testId), eq(jwtToken));
    }

    @Test
    void updateDeletePostById_ValidFalseArguments_ReturnsSuccessStatus() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // Arrange
        Long testId = 1L;
        UpdateDeletedDto updateDeletedDto = new UpdateDeletedDto(false);
        String updateDeletedDtoJson = objectMapper.writeValueAsString(updateDeletedDto);

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        doNothing()
                .when(postService).updateDeletePostById(any(UpdateDeletedDto.class), eq(testId), eq(jwtToken));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/delete/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDeletedDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).updateDeletePostById(any(UpdateDeletedDto.class), eq(testId), eq(jwtToken));
    }

    @Test
    void updateDeletePostById_InvalidMissingBoolean_ReturnsSuccessStatus() throws Exception {
        // Mocked resources: commonService, jwtToken, postService

        // should still return success because wantsToDelete is boolean, and no value defaults to false

        // Arrange
        Long testId = 1L;
        String updateDeletedDtoJson = "{}";

        when(commonService.getJwt(any(Authentication.class)))
                .thenReturn(jwtToken);
        doNothing()
                .when(postService).updateDeletePostById(any(UpdateDeletedDto.class), eq(testId), eq(jwtToken));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/delete/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDeletedDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        verify(commonService, times(1)).getJwt(any(Authentication.class));
        verify(postService, times(1)).updateDeletePostById(any(UpdateDeletedDto.class), eq(testId), eq(jwtToken));
    }

    @Test
    void updateDeletePostById_InvalidBoolean_ReturnsBadRequestStatus() throws Exception {
        // Mocked resources: commonService, postService

        // Arrange
        Long testId = 1L;
        String updateDeletedDtoJson = "{\"wantsToDelete\": \"asdasdasdadasdasd\"}";

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/delete/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDeletedDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        verify(commonService, never()).getJwt(any(Authentication.class));
        verify(postService, never()).updateDeletePostById(any(UpdateDeletedDto.class), any(Long.class), any(Jwt.class));
    }

    @Test
    void updateDeletePostById_InvalidId_ReturnsBadRequestStatus() throws Exception {
        /// Mocked resources: commonService, postService

        // Arrange
        Long testId = 0L;
        UpdateDeletedDto updateDeletedDto = new UpdateDeletedDto(false);
        String updateDeletedDtoJson = objectMapper.writeValueAsString(updateDeletedDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/delete/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDeletedDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        verify(commonService, never()).getJwt(any(Authentication.class));
        verify(postService, never()).updateDeletePostById(any(UpdateDeletedDto.class), any(Long.class), any(Jwt.class));
    }

}
