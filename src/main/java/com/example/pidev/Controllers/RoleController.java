package com.example.pidev.Controllers;


import com.example.pidev.Entities.Role;
import com.example.pidev.Entities.User;
import com.example.pidev.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/addRole")
    public Role addRole(@RequestBody Role role) {
        Role  r = roleService.createNewRole(role);
        return r;
    }

    @GetMapping("/retrieve-all-Roles")
    public List<Role> getRoles() {
        List<Role> listRoles = roleService.retrieveAllRoles();
        return listRoles;

    }

}
