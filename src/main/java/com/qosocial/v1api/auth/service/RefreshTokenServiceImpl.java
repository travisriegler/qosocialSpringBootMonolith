package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.exception.GenericDeleteRefreshTokenException;
import com.qosocial.v1api.auth.exception.GenericSaveRefreshTokenException;
import com.qosocial.v1api.auth.model.RefreshTokenModel;
import com.qosocial.v1api.auth.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(RefreshTokenModel refreshTokenModel) {
        try {
            refreshTokenRepository.save(refreshTokenModel);
        } catch (Exception ex) {
            logger.error("RefreshTokenServiceImpl saveRefreshToken encountered an exception trying to save refresh token: " + refreshTokenModel.getTokenId(), ex);
            throw new GenericSaveRefreshTokenException(ex);
        }
    }

    @Override
    public void deleteRefreshToken(String tokenId) {
        try {
            refreshTokenRepository.deleteById(tokenId);
        } catch (Exception ex) {
            logger.error("RefreshTokenServiceImpl deleteRefreshToken encountered an exception trying to delete refresh token: " + tokenId, ex);
            throw new GenericDeleteRefreshTokenException(ex);
        }
    }
}
