package com.qosocial.v1api.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qosocial.v1api.common.dto.ErrorResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class GlobalFilterExceptionHandler extends OncePerRequestFilter {

    /**
     * Flowchart:
     * 1. Try to do entire api response
     * 2. If there is an exception NOT CAUGHT by the GlobalControllerExceptionHandler, we will catch it and execute handleException
     * 3. handleException will try to create the errorResponseDto, try to convert it to JSON, and try to send the response with sendErrorResponse
     * 4. If there is an exception again, we will catch it and execute sendFallbackErrorResponse
     * 5. If there is an exception AGAIN, there is nothing we can do further
     */

    private static final Logger logger = LoggerFactory.getLogger(GlobalFilterExceptionHandler.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public GlobalFilterExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handleException(ex, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleException(Exception e, HttpServletResponse response, HttpStatus status) {
        // Log the exception
        logger.error("Exception caught in filter: ", e);

        try {
            // Prepare the error response body similar to CustomErrorAttributes structure
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(e.getMessage()));
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
        response.getWriter().write(jsonErrorResponse);
    }

    private void sendFallbackErrorResponse(HttpServletResponse response, HttpStatus status) {
        try {
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"message\": \"An unexpected error occurred.\"}");
        } catch (IOException ioException) {
            logger.error("Error while writing fallback error response: ", ioException);
        }
    }
}
