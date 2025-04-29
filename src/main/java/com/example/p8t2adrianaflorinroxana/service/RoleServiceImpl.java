package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Roles;
import com.example.p8t2adrianaflorinroxana.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Roles findByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
