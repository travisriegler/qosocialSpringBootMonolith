package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.dto.LoginDto;
import com.qosocial.v1api.auth.dto.LoginResponseDto;
import com.qosocial.v1api.auth.dto.RegisterDto;
import com.qosocial.v1api.auth.model.AppUserModel;

public interface AuthService {
    public void register(RegisterDto registerDto);
    public LoginResponseDto login(LoginDto loginDto);

    public LoginResponseDto refreshToken(String refreshToken);

    public void logout(String refreshToken);

    public AppUserModel findById(Long id);
}
