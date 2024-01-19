package com.qosocial.v1api.auth.service;

import com.qosocial.v1api.auth.exception.GenericFindRoleException;
import com.qosocial.v1api.auth.exception.RoleNotFoundException;
import com.qosocial.v1api.auth.model.RoleModel;
import com.qosocial.v1api.auth.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleModel findRoleByName(String name) {
        try {
            return roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException("Unable to find role: " + name));
        } catch (RoleNotFoundException ex) {
            logger.warn("RoleServiceImpl findRoleByName could not find this role: " + name);
            throw ex;
        } catch (Exception ex) {
            logger.error("RoleServiceImpl findByName caught an unexpected error", ex);
            throw new GenericFindRoleException(ex);
        }
    }
}
