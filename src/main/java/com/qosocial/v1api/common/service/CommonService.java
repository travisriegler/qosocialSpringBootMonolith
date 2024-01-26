package com.qosocial.v1api.common.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {
    public Jwt getJwt(Authentication authentication);
    public Long getLongUserId(Jwt jwtToken);
    public void validateImageFile(MultipartFile imageFile);
}
