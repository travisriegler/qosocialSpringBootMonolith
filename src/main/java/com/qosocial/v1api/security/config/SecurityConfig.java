package com.qosocial.v1api.security.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Qualifier("accessTokenJwtDecoder")
    private final JwtDecoder accessTokenJwtDecoder;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, JwtDecoder accessTokenJwtDecoder) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.accessTokenJwtDecoder = accessTokenJwtDecoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowCredentials(true);
                            config.addAllowedOriginPattern("https://v1staging.qosocial.com");
                            config.addAllowedOriginPattern("https://v1.qosocial.com");
                            config.addAllowedOriginPattern("http://localhost:*");
                            config.addAllowedMethod("*");
                            config.addAllowedHeader("*");
                            return config;
                        })
                )
                .headers(h -> h
                        .contentSecurityPolicy(p -> p
                                .policyDirectives("frame-ancestors 'self'")
                        )
                )
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/healthcheck").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .jwt(jwt -> jwt.decoder(accessTokenJwtDecoder))
                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

