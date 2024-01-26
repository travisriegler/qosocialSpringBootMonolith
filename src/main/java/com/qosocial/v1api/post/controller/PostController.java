package com.qosocial.v1api.post.controller;

import com.qosocial.v1api.common.service.CommonService;
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

    private static final int DEFAULT_LIMIT = 10;
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 10;
    private final PostService postService;
    private final CommonService commonService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService, CommonService commonService) {
        this.postService = postService;
        this.commonService = commonService;
    }

    @PostMapping()
    public ResponseEntity<Void> createPost(@RequestBody @Validated CreatePostDto createPostDto, Authentication authentication) {

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        postService.createPost(createPostDto, jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) String offset, @RequestParam(required = false) String limit, Authentication authentication) {

        Instant timeStamp = parseOffset(offset);

        int parsedLimit = parseLimit(limit);

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        List<PostDto> postDtos = postService.getAllPosts(timeStamp, parsedLimit, jwtToken);

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<List<PostDto>> getPostsByProfileId(@RequestParam(required = false) String offset, @RequestParam(required = false) String limit, @PathVariable Long id, Authentication authentication) {

        Instant timeStamp = parseOffset(offset);

        int parsedLimit = parseLimit(limit);

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        List<PostDto> postDtos = postService.getPostsByProfileId(timeStamp, parsedLimit, id, jwtToken);

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> updateDeletePostById(@RequestBody @Validated UpdateDeletedDto updateDeletedDto, @PathVariable Long id, Authentication authentication) {

        // Safely checks authentication and jwtToken to ensure they are not null
        Jwt jwtToken = commonService.getJwt(authentication);

        postService.updateDeletePostById(updateDeletedDto, id, jwtToken);

        return ResponseEntity.ok().build();
    }

    private int parseLimit(String limit) {
        try {
            int parsedLimit = Integer.parseInt(limit);
            if (parsedLimit >= MIN_LIMIT && parsedLimit <= MAX_LIMIT) {
                return parsedLimit;
            } else {
                logger.warn("PostController received an out-of-range limit value: " + limit);
            }
        } catch (Exception ex) {
            logger.warn("PostController received an invalid limit value: " + limit);
        }
        return DEFAULT_LIMIT;
    }

    private Instant parseOffset(String offset) {
        try {
            return Instant.parse(offset);
        } catch (Exception ex) {
            logger.warn("PostController received an invalid offset value: " + offset);
            return Instant.now();
        }
    }
}
