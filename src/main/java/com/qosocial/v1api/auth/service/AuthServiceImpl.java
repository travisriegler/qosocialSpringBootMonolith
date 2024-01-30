package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.dto.*;
import com.qosocial.v1api.auth.exception.*;
import com.qosocial.v1api.auth.mapper.AppUserMapper;
import com.qosocial.v1api.auth.model.AppUserModel;
import com.qosocial.v1api.auth.model.RefreshTokenModel;
import com.qosocial.v1api.auth.model.RoleModel;
import com.qosocial.v1api.auth.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AppUserMapper appUserMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(AppUserRepository appUserRepository, RoleService roleService, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder, AppUserMapper appUserMapper, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.roleService = roleService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.appUserMapper = appUserMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public void register(RegisterDto registerDto) {

        try {

            // Check for duplicate email
            if (appUserRepository.existsByEmailIgnoreCase(registerDto.getEmail())) { throw new EmailAlreadyExistsException(); }

            AppUserModel newUser = appUserMapper.fromRegisterDtoToAppUserModel(registerDto);

            String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
            newUser.setPassword(encryptedPassword);

            RoleModel existingDefaultRole = roleService.findRoleByName("ROLE_USER");
            newUser.getRoleModels().add(existingDefaultRole);

            appUserRepository.save(newUser);

        } catch (EmailAlreadyExistsException ex) {
            logger.warn("Requested email " + registerDto.getEmail() + " already exists");
            throw ex;
        } catch (Exception ex) {
            logger.error("AuthServiceImpl register caught an unexpected error", ex);
            throw new GenericRegistrationException(ex);
        }
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            Authentication authResult = authenticationManager.authenticate(authRequest);

            SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();
            AppUserModel appUserModel = securityUser.getAppUserModel();

            // Generate the accessToken
            String accessToken = jwtService.generateAccessToken(appUserModel.getId(), appUserModel.getEmail(), appUserModel.getRoleModels());

            // Generate the refreshToken
            RefreshTokenDto refreshTokenDto = jwtService.generateRefreshToken(appUserModel.getId(), appUserModel.getEmail());

            // Create a RefreshTokenModel to save it in the db
            refreshTokenService.saveRefreshToken(new RefreshTokenModel(refreshTokenDto.getTokenId(), refreshTokenDto.getExpirationTime(), appUserModel));

            //Convert Set<RoleModel> to Set<String> so it is easier for the frontend
            Set<String> scopeSet = appUserModel.getRoleModels().stream()
                    .map(RoleModel::getName)
                    .collect(Collectors.toSet());

            return new LoginResponseDto(
                    accessToken,
                    appUserModel.getId(),
                    appUserModel.getEmail(),
                    scopeSet,
                    refreshTokenDto.getRefreshToken());

        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            throw new BadCredentialsException("Incorrect email or password, please try again");
        } catch (DisabledException | LockedException | AccountExpiredException | CredentialsExpiredException ex) {
            logger.warn("Login attempt with disabled/locked/expired account: " + loginDto.getEmail(), ex);
            throw ex;
        } catch (GenericJwtGenerationException | GenericSaveRefreshTokenException ex) {
            //already been logged at lower levels
            throw new GenericLoginException(ex);
        } catch (Exception ex) {
            logger.error("AuthServiceImpl login caught an unexpected exception", ex);
            throw new GenericLoginException(ex);
        }
    }


    @Override
    @Transactional
    public LoginResponseDto refreshToken(String refreshToken) {

        try {

            //Step 1: Decode the refresh token
            Jwt jwt = jwtService.decodeRefreshToken(refreshToken); //can throw ExpiredRefreshTokenException or InvalidRefreshTokenException

            if (jwt == null) throw new InvalidRefreshTokenException();

            String tokenId = jwt.getClaimAsString("tokenId");

            if (tokenId == null || tokenId.isEmpty()) throw new InvalidRefreshTokenException();



            //Step 2: Delete the refresh token
            refreshTokenService.deleteRefreshToken(tokenId); //can throw GenericDeleteRefreshTokenException



            //Step 3: Create a new access token and refresh token
            String email = jwt.getClaimAsString("email");

            if (email == null || email.isEmpty()) throw new InvalidRefreshTokenException();

            AppUserModel appUserModel = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + jwt.getSubject()));

            if (!appUserModel.isEnabled() || !appUserModel.isAccountNonExpired() || !appUserModel.isCredentialsNonExpired() || !appUserModel.isAccountNonLocked()) {
                throw new ExpiredRefreshTokenException();
            }

            String accessToken = jwtService.generateAccessToken(appUserModel.getId(), appUserModel.getEmail(), appUserModel.getRoleModels());

            RefreshTokenDto refreshTokenDto = jwtService.generateRefreshToken(appUserModel.getId(), appUserModel.getEmail());



            //Step 4: Save the new refresh token in the db
            refreshTokenService.saveRefreshToken(new RefreshTokenModel(refreshTokenDto.getTokenId(), refreshTokenDto.getExpirationTime(), appUserModel));



            //Step 5: Return information
            //Convert Set<RoleModel> to Set<String> so it is easier for the frontend
            Set<String> scopeSet = appUserModel.getRoleModels().stream()
                    .map(RoleModel::getName)
                    .collect(Collectors.toSet());

            return new LoginResponseDto(
                    accessToken,
                    appUserModel.getId(),
                    appUserModel.getEmail(),
                    scopeSet,
                    refreshTokenDto.getRefreshToken());

        } catch (GenericDeleteRefreshTokenException | GenericJwtGenerationException | GenericSaveRefreshTokenException ex) {
            //logged already
            throw new ExpiredRefreshTokenException(ex);
        } catch (ExpiredRefreshTokenException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("AuthServiceImpl refreshToken caught an unexpected error", ex);
            throw new ExpiredRefreshTokenException(ex);
        }
    }

    @Override
    public void logout(String refreshToken) {
        //authController /logout already checked if the refresh token was null or empty
        try {
            Jwt jwt = jwtService.decodeRefreshToken(refreshToken);
            // we do not care if the refresh token is expired (we would care elsewhere)

            // if jwt is null, return early
            if (jwt == null) return;

            String tokenId = jwt.getClaimAsString("tokenId");

            // if tokenId is null or empty, return early
            if (tokenId == null || tokenId.isEmpty()) return;

            refreshTokenService.deleteRefreshToken(tokenId);

        } catch (InvalidRefreshTokenException | GenericDeleteRefreshTokenException ex) {
            // exception was already logged
            throw new GenericLogoutException();
        }
        catch (Exception ex) {
            logger.error("AuthServiceImpl logout caught an unexpected exception", ex);
            throw new GenericLogoutException(ex);
        }
    }

    @Override
    public AppUserModel findById(Long id) {
        try {
            return appUserRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Unable to find the user, please try again"));
        } catch (UsernameNotFoundException ex) {
            logger.warn("AuthServiceImpl findById could not find this user: " + id);
            throw ex;
        } catch (Exception ex) {
            logger.error("AuthServiceImpl findById caught an unexpected error", ex);
            throw new GenericFindUserException(ex);
        }
    }
}
