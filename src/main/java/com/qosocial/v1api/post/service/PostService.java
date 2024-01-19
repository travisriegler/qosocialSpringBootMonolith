package com.qosocial.v1api.post.service;

import com.qosocial.v1api.post.dto.CreatePostDto;
import com.qosocial.v1api.post.dto.PostDto;
import com.qosocial.v1api.post.dto.UpdateDeletedDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

public interface PostService {
    public void createPost(CreatePostDto createPostDto, Jwt jwtToken);

    public List<PostDto> getAllPosts(Instant timeStamp, int limit, Jwt jwtToken);

    public List<PostDto> getPostsByProfileId(Instant timeStamp, int limit, Long profileId, Jwt jwtToken);

    public void updateDeletePostById(UpdateDeletedDto updateDeletedDto, Long postId, Jwt jwtToken);
}
