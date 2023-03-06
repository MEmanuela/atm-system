package org.internship.jpaonlinebanking.controllers;

import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.services.RoleService;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    @GetMapping("/getAllRoles")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/role")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }
    @GetMapping("/{roleId}/users")
    public List<User> getUsersByRole(@PathVariable(value = "roleId") Long roleId) {
        return userService.getUsersByRole(roleId);
    }

}
