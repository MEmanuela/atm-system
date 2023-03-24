package org.internship.jpaonlinebanking.controllers;

import jakarta.validation.Valid;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.AuthorizationException;
import org.internship.jpaonlinebanking.security.PasswordUpdateRequest;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/v1")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @GetMapping("/getAllUsers")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/{roleId}/user")
    public User createUser(@PathVariable(value = "roleId") Long roleId,
                           @Valid @RequestBody User user) {
        return userService.createUser(roleId, user);
    }
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        // comment from main
        userService.deleteUserById(userId);
    }
    @PostMapping("/{userId}/{typeId}/account")
    public Account createAccount(@PathVariable(value = "typeId") Long typeId,
                                 @Valid @RequestBody Account account,
                                 @PathVariable(value = "userId") Long userId) {
        return accountService.createAccount(typeId, account, userId);
    }
    @DeleteMapping("/account/{accountName}")
    public void deleteAccount(@PathVariable(value = "accountName") String name) {
        accountService.deleteAccountByName(name);
    }
    @PutMapping("/{userId}/password")
    public void updatePassword(@PathVariable(value = "userId") Long userId,
                               @Valid @RequestBody PasswordUpdateRequest request,
                               @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to change user's password");
        }
        userService.updateUserPassword(userId, request.getPassword());
    }
}
