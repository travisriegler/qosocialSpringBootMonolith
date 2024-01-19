package com.qosocial.v1api.post.controller;

import com.qosocial.v1api.common.util.CommonUtil;
import com.qosocial.v1api.post.dto.CreatePostDto;
import com.qosocial.v1api.post.dto.PostDto;
import com.qosocial.v1api.post.dto.UpdateDeletedDto;
import com.qosocial.v1api.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<Void> createPost(@RequestBody @Validated CreatePostDto createPostDto, Authentication authentication) {

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        postService.createPost(createPostDto, jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) String offset, @RequestParam(defaultValue = "10") int limit, Authentication authentication) {

        Instant timeStamp;
        try {
            timeStamp = Instant.parse(offset);
        } catch (Exception e) {
            logger.warn("PostController getAllPosts received an invalid offset value: " + offset);
            timeStamp = Instant.now();
        }

        limit = (limit < 1 || limit > 10) ? 10 : limit;

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        List<PostDto> postDtos = postService.getAllPosts(timeStamp, limit, jwtToken);

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<List<PostDto>> getPostsByProfileId(@RequestParam(required = false) String offset, @RequestParam(defaultValue = "10") int limit, @PathVariable Long id, Authentication authentication) {

        Instant timeStamp;
        try {
            timeStamp = Instant.parse(offset);
        } catch (Exception e) {
            logger.warn("PostController getPostsByProfileId received an invalid offset value: " + offset);
            timeStamp = Instant.now();
        }

        limit = (limit < 1 || limit > 10) ? 10 : limit;

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        List<PostDto> postDtos = postService.getPostsByProfileId(timeStamp, limit, id, jwtToken);

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> updateDeletePostById(@RequestBody @Validated UpdateDeletedDto updateDeletedDto, @PathVariable Long id, Authentication authentication) {

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = CommonUtil.getJwt(authentication);

        postService.updateDeletePostById(updateDeletedDto, id, jwtToken);

        return ResponseEntity.ok().build();
    }
}
