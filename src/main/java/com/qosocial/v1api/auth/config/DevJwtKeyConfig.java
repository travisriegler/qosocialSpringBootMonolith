package com.qosocial.v1api.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Configuration
@Profile("dev")
public class DevJwtKeyConfig {

    private static final Logger logger = LoggerFactory.getLogger(DevJwtKeyConfig.class);

    @Bean
    @Qualifier("accessTokenKeyPair")
    public KeyPair accessTokenKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair();
    }

    @Bean
    @Qualifier("refreshTokenKeyPair")
    public KeyPair refreshTokenKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair();
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}
