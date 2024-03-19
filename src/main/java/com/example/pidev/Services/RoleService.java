package com.example.pidev.Services;

import com.example.pidev.Entities.Role;
import com.example.pidev.Interfaces.IRoleService;
import com.example.pidev.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService implements IRoleService {


    @Autowired
    private RoleRepository roleRepository ;


    public Role createNewRole(Role role) {
        return roleRepository.save(role);
    }


    public List<Role> retrieveAllRoles() {
        return roleRepository.findAll();
    }

}
