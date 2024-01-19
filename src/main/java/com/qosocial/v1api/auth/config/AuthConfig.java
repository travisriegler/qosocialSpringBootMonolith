package com.qosocial.v1api.auth.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AuthConfig {
    private final UserDetailsService userDetailsService;
    private final RSAPublicKey accessTokenPublicKey;
    private final RSAPrivateKey accessTokenPrivateKey;
    private final RSAPublicKey refreshTokenPublicKey;
    private final RSAPrivateKey refreshTokenPrivateKey;

    @Autowired
    public AuthConfig(UserDetailsService userDetailsService, @Qualifier("accessTokenKeyPair") KeyPair accessTokenKeyPair, @Qualifier("refreshTokenKeyPair") KeyPair refreshTokenKeyPair) {

        Assert.notNull(accessTokenKeyPair, "accessTokenKeyPair must not be null");
        Assert.notNull(refreshTokenKeyPair, "refreshTokenKeyPair must not be null");

        this.userDetailsService = userDetailsService;

        this.accessTokenPublicKey = (RSAPublicKey) accessTokenKeyPair.getPublic();
        this.accessTokenPrivateKey = (RSAPrivateKey) accessTokenKeyPair.getPrivate();

        this.refreshTokenPublicKey = (RSAPublicKey) refreshTokenKeyPair.getPublic();
        this.refreshTokenPrivateKey = (RSAPrivateKey) refreshTokenKeyPair.getPrivate();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder(65536, 8, 1, 32, 64));
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    @Bean
    @Qualifier("accessTokenJwtDecoder")
    public JwtDecoder accessTokenJwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(accessTokenPublicKey).build();
    }

    @Bean
    @Qualifier("accessTokenJwtEncoder")
    public JwtEncoder accessTokenJwtEncoder() {
        JWK jwk = new RSAKey.Builder(accessTokenPublicKey).privateKey(accessTokenPrivateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Qualifier("refreshTokenJwtDecoder")
    public JwtDecoder refreshTokenJwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(refreshTokenPublicKey).build();
    }

    @Bean
    @Qualifier("refreshTokenJwtEncoder")
    public JwtEncoder refreshTokenJwtEncoder() {
        JWK jwk = new RSAKey.Builder(refreshTokenPublicKey).privateKey(refreshTokenPrivateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
