package com.qosocial.v1api.auth.controller;

import com.qosocial.v1api.auth.dto.LoginDto;
import com.qosocial.v1api.auth.dto.LoginResponseDto;
import com.qosocial.v1api.auth.dto.RegisterDto;
import com.qosocial.v1api.auth.exception.GenericLogoutException;
import com.qosocial.v1api.auth.exception.NoRefreshTokenException;
import com.qosocial.v1api.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody @Validated RegisterDto registerDto, HttpServletResponse response) {
        clearRefreshTokenCookie(response);
        authService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginDto loginDto, HttpServletResponse response) {

        clearRefreshTokenCookie(response);

        LoginResponseDto loginResponseDto = authService.login(loginDto);

        createRefreshTokenCookie(loginResponseDto.getRefreshToken(), response);

        loginResponseDto.setRefreshToken("Success");

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = extractRefreshTokenFromCookie(request);

        clearRefreshTokenCookie(response);

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new NoRefreshTokenException();
        }

        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);

        createRefreshTokenCookie(loginResponseDto.getRefreshToken(), response);

        loginResponseDto.setRefreshToken("Success");

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);

    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        try {
            String refreshToken = extractRefreshTokenFromCookie(request);
            if (refreshToken != null && !refreshToken.isEmpty()) {
                authService.logout(refreshToken);
            }
        } catch (GenericLogoutException ex) {
            // Do nothing for GenericLogoutException, it was already logged
        } catch (Exception ex) {
            logger.error("AuthController logout caught an exception", ex);
        }

        clearRefreshTokenCookie(response);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    private void createRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // e.g., 30 days expiration
        response.addCookie(refreshTokenCookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        // Extract the cookies from the request
        Cookie[] cookies = request.getCookies();

        // if there are no cookies, return null
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        // Cookies are present, now check if we have a refreshToken cookie
        // if we don't have a refreshToken cookie, return null
        return Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // Effectively deletes the cookie
        response.addCookie(refreshTokenCookie);
    }

}
