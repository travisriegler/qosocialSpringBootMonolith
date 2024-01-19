package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.dto.SecurityUser;
import com.qosocial.v1api.auth.model.AppUserModel;
import com.qosocial.v1api.auth.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        try {
            AppUserModel appUserModel = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            return new SecurityUser(appUserModel);

        } catch (UsernameNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("UserDetailsServiceImpl caught an unexpected exception", ex);
            throw new AuthenticationServiceException("An unexpected error occurred during authentication", ex);
        }
    }
}
