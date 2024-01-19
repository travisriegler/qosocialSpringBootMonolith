package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.model.RefreshTokenModel;

public interface RefreshTokenService {
    public void saveRefreshToken(RefreshTokenModel refreshTokenModel);
    public void deleteRefreshToken(String refreshToken);
}
