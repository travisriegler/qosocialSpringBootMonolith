package com.qosocial.v1api.auth.mapper;

import com.qosocial.v1api.auth.dto.RegisterDto;
import com.qosocial.v1api.auth.model.AppUserModel;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUserModel fromRegisterDtoToAppUserModel(RegisterDto registerDto) {
        AppUserModel appUserModel = new AppUserModel();

        appUserModel.setEmail(registerDto.getEmail());
        appUserModel.setAcceptedTerms(registerDto.hasAcceptedTerms());

        appUserModel.setEnabled(true);
        appUserModel.setCredentialsNonExpired(true);
        appUserModel.setAccountNonExpired(true);
        appUserModel.setAccountNonLocked(true);

        return appUserModel;
    }

}

