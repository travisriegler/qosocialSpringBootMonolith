package com.qosocial.v1api.common.service;

import com.qosocial.v1api.common.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService{

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Override
    public Jwt getJwt(Authentication authentication) {

        // Check if authentication is null
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication is missing");
        }

        // Check if authentication is NOT a JwtAuthenticationToken
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new InvalidAuthenticationTypeException();
        }

        // Convert authentication to JwtAuthenticationToken
        Jwt jwtToken = ((JwtAuthenticationToken) authentication).getToken();

        // Check if the jwtToken is null
        if (jwtToken == null) {
            throw new JwtTokenMissingException();
        }

        // Check that the issuer is as expected
        URL issuerUrl = jwtToken.getIssuer();
        String issuer = issuerUrl != null ? issuerUrl.toString() : null;
        if (!"http://self.com".equals(issuer)) {
            throw new InvalidJwtIssuerException();
        }

        // Validate the audience claim
        List<String> audiences = jwtToken.getAudience();
        if (audiences == null || !audiences.contains("my app")) {
            throw new InvalidJwtAudienceException();
        }

        return jwtToken;
    }

    @Override
    public Long getLongUserId(Jwt jwtToken) {
        // Get the subject from jwtToken
        String stringUserId = jwtToken.getSubject();

        // Ensure stringUserId is not null and not empty
        if (stringUserId == null || stringUserId.trim().isEmpty()) {
            throw new InvalidJwtSubjectException();
        }

        // Try to parse stringUserId into a Long. Could throw NumberFormatException.
        return Long.parseLong(stringUserId);
    }

    @Override
    public void validateImageFile(MultipartFile imageFile) {

        // images are optional. If we are not given an image, that is fine.
        //But if we are given an image and there is an issue with it, we need to throw an exception
        if (imageFile != null && !imageFile.isEmpty()) {

            String contentType = imageFile.getContentType();

            // If contentType is null or does not start with "image/", throw an exception
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new InvalidImageTypeException();
            }
        }
    }
}
