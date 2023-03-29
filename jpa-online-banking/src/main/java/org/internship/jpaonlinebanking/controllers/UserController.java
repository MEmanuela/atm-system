package org.internship.jpaonlinebanking.controllers;

import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/getAllUsers")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/{roleId}/user")
    public User createUser(@PathVariable(value = "roleId") Long roleId,
                           @RequestBody User user) {
        return userService.createUser(roleId, user);
    }

//    @GetMapping("/user/{userId}")
//    public User getUserById(@PathVariable(value = "userId") Long userId) {
//        return userService.getUserById(userId);
//    }

    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        userService.deleteUserById(userId);
    }

    // update user's password
    @PutMapping("/{userId}/password")
    public void updatePassword(@PathVariable(value = "userId") Long userId,
                               @RequestBody String password) {
        userService.updateUserPassword(userId, password);
    }

}
