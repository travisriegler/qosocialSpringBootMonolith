package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.model.RoleModel;

public interface RoleService {
    RoleModel findRoleByName(String name);
}
