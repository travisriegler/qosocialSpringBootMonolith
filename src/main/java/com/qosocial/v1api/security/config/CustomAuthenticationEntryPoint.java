package com.qosocial.v1api.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qosocial.v1api.common.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //InsufficientAuthenticationException -> send message 1
        //InvalidBearerTokenException -> send message 2

        String message;

        if (authException instanceof InsufficientAuthenticationException) {
            message = "Unauthorized, no access token provided";
        } else {
            message = "Forbidden, access token expired or invalid";
        }

        handleException(message, response, HttpStatus.UNAUTHORIZED);
    }


    private void handleException(String message, HttpServletResponse response, HttpStatus status) {

        try {
            // Prepare the error response body similar to CustomErrorAttributes structure
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(message));
            String jsonErrorResponseDto = convertToJson(errorResponseDto);
            sendErrorResponse(response, jsonErrorResponseDto, status);
        } catch (Exception ex) {
            logger.error("Error handling exception: ", ex);
            sendFallbackErrorResponse(response, status);
        }
    }

    private String convertToJson(ErrorResponseDto errorResponseDto) {
        try {
            return objectMapper.writeValueAsString(errorResponseDto);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting to JSON", e);
            throw new RuntimeException("Error converting error response to JSON", e);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String jsonErrorResponse, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonErrorResponse);
    }

    private void sendFallbackErrorResponse(HttpServletResponse response, HttpStatus status) {
        try {
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"An unexpected error occurred.\"}");
        } catch (IOException ioException) {
            logger.error("Error while writing fallback error response: ", ioException);
        }
    }
}
